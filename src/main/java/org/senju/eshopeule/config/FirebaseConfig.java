package org.senju.eshopeule.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.storage-bucket}")
    private String firebaseStorageBucket;

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        String serviceAccountPath = System.getenv("FIREBASE_KEY_PATH") + "eshopeule-firebase-key.json";
        FileInputStream serviceAccountStream = new FileInputStream(serviceAccountPath);
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                .setStorageBucket(firebaseStorageBucket)
                .build();
        return FirebaseApp.initializeApp(options);
    }
}
