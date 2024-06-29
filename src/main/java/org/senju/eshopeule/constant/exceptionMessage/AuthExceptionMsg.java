package org.senju.eshopeule.constant.exceptionMessage;

public final class AuthExceptionMsg {
    public static final String SIGNUP_ERROR_MSG = "Sign Up error.";
    public static final String LOGIN_ERROR_MSG = "Login error.";
    public static final String REFRESH_TOKEN_ERROR_MSG = "Refresh token error.";
    public static final String CHANGE_PASSWORD_ERROR_MSG = "Change password error.";
    public static final String VERIFICATION_CODE_ERROR = "Verification code is invalid";

    public static final String JWT_MISSING_ERROR_MSG = "No JWT token found in request headers";
    public static final String JWT_TOKEN_EXPIRED_ERROR_MSG = "JWT token expired.";
    public static final String JWT_TOKEN_INVALID_ERROR_MSG = "JWT token invalid.";
}
