package rw.autotrack.autotrack360.auth;

import lombok.Data;
import rw.autotrack.autotrack360.user.Role;

public class AuthDTOs {

    @Data
    public static class RegisterRequest {
        private String name;
        private String email;
        private String phone;
        private String password;
        private Role role;
    }

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    public static class AuthResponse {
        private String token;
        private String email;
        private String role;
        private String name;

        public AuthResponse(String token, String email, String role, String name) {
            this.token = token;
            this.email = email;
            this.role = role;
            this.name = name;
        }
    }

    @Data
    public static class ForgotPasswordRequest {
        private String email;
    }

    @Data
    public static class ResetPasswordRequest {
        private String email;
        private String otp;
        private String newPassword;
    }
}
