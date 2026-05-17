package rw.autotrack.autotrack360.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthDTOs.AuthResponse> register(@RequestBody AuthDTOs.RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDTOs.AuthResponse> login(@RequestBody AuthDTOs.LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody AuthDTOs.ForgotPasswordRequest req) {
        authService.forgotPassword(req);
        return ResponseEntity.ok(Map.of("message", "OTP sent to your registered phone/email"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody AuthDTOs.ResetPasswordRequest req) {
        authService.resetPassword(req);
        return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
    }
}
