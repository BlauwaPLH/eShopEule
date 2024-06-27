package org.senju.eshopeule.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.constant.enums.NotificationType;
import org.senju.eshopeule.dto.NotificationDTO;
import org.senju.eshopeule.dto.request.LoginRequest;
import org.senju.eshopeule.exceptions.SendNotificationException;
import org.senju.eshopeule.repository.TokenRepository;
import org.senju.eshopeule.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

import static org.senju.eshopeule.constant.enums.NotificationType.ContentType.VERIFY_SIGNUP;
import static org.senju.eshopeule.constant.enums.NotificationType.SendMethodType.EMAIL;

@RestController
@RequiredArgsConstructor
public class DemoController {

    private final TokenRepository tokenRepository;
    private final NotificationService SMSNotificationService;
    private final NotificationService emailNotificationService;

    @PostMapping(path = "/api/r/v1/demo")
    public ResponseEntity<Map<?, ?>> demo(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(
                Map.of("message", "OKKKKKKKKKKKKK!")
        );
    }

    @GetMapping(path = "/api/r/v1/demo")
    public ResponseEntity<Map<?, ?>> demo() {
        return ResponseEntity.ok(
                Map.of("message", "OKKKKKKKKKKKKK!")
        );
    }

    @GetMapping(path = "/api/p/v1/twilio")
    public ResponseEntity<?> testTwilio() {
        try {
            SMSNotificationService.sendNotification(
                    new NotificationDTO(NotificationType.builder().build(), "hello chanspech", "0968950819")
            );
            return ResponseEntity.ok(Collections.singletonMap("message", "send SMS successfully"));
        } catch (SendNotificationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Collections.singletonMap("message", ex.getMessage())
            );
        }
    }

    @GetMapping(path = "/api/p/v1/demo/sendgrid")
    public ResponseEntity<?> testSendGrid() {
        try {
            NotificationType notificationType = NotificationType.builder()
                    .sendMethodType(EMAIL)
                    .contentType(VERIFY_SIGNUP)
                    .build();
            NotificationDTO notificationDTO = new NotificationDTO(notificationType, "test", "haivu1921@gmail.com");
            emailNotificationService.sendNotification(notificationDTO);
            return ResponseEntity.ok(Collections.singletonMap("message", "send email successfully"));
        } catch (SendNotificationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/csrf")
    public CsrfToken csrf(CsrfToken csrfToken) {
        return csrfToken;
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "this is Admin page!";
    }

}
