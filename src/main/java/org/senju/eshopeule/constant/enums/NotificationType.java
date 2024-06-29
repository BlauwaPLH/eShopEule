package org.senju.eshopeule.constant.enums;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class NotificationType {

    private SendMethodType sendMethodType;
    private ContentType contentType;

    @Getter
    @RequiredArgsConstructor
    public enum SendMethodType {
        EMAIL("email"),
        SMS("sms");

        private final String methodName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ContentType {
        FORGOT_PASSWORD("Reset password"),
        VERIFY_SIGNUP("Verify register account");

        private final String contentName;
    }
}
