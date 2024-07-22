package org.senju.eshopeule.utils;

import java.security.SecureRandom;

import static org.senju.eshopeule.constant.pattern.RegexPattern.PASSWORD_PATTERN;

public class StringGeneratorUtil {

    public static String generateRandomCode(int length) {
        final String numbers = "0123456789";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(numbers.length());
            code.append(numbers.charAt(randomIndex));
        }
        return code.toString();
    }

    public static String generatePassword() {
        final String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@$!%*?&";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int randomIndex = secureRandom.nextInt(validChars.length());
            password.append(validChars.charAt(randomIndex));
        }

        if (!password.toString().matches(PASSWORD_PATTERN)) return generatePassword();
        return password.toString();
    }
}
