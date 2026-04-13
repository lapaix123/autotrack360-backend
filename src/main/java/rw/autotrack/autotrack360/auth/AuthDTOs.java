package rw.autotrack.autotrack360.auth;

import lombok.Data;
import rw.autotrack.autotrack360.user.Role;

public class AuthDTOs {

    @Data
    public static class RegisterRequest {
        private String name;
        private String email;
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

        public AuthResponse(String token, String email, String role) {
            this.token = token;
            this.email = email;
            this.role = role;
        }
    }
}
