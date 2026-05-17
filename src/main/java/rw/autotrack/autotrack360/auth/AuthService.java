package rw.autotrack.autotrack360.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rw.autotrack.autotrack360.notification.SmsService;
import rw.autotrack.autotrack360.security.JwtUtil;
import rw.autotrack.autotrack360.user.User;
import rw.autotrack.autotrack360.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final SmsService smsService;

    public AuthDTOs.AuthResponse register(AuthDTOs.RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole())
                .build();
        userRepository.save(user);

        if (req.getPhone() != null && !req.getPhone().isBlank()) {
            smsService.sendWelcome(req.getPhone(), req.getName());
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthDTOs.AuthResponse(token, user.getEmail(), user.getRole().name(), user.getName());
    }

    public AuthDTOs.AuthResponse login(AuthDTOs.LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthDTOs.AuthResponse(token, user.getEmail(), user.getRole().name(), user.getName());
    }

    public void forgotPassword(AuthDTOs.ForgotPasswordRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("No account found with this email"));

        String otp = String.format("%06d", new Random().nextInt(999999));
        user.setResetOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        // Send via SMS if phone exists, always log to console
        if (user.getPhone() != null && !user.getPhone().isBlank()) {
            smsService.sendPasswordReset(user.getPhone(), otp);
        } else {
            // Fallback: log OTP (in production send via email)
            System.out.println("[PASSWORD-RESET] OTP for " + req.getEmail() + " → " + otp);
        }
    }

    public void resetPassword(AuthDTOs.ResetPasswordRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("No account found with this email"));

        if (user.getResetOtp() == null || !user.getResetOtp().equals(req.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }
        if (user.getOtpExpiry() == null || LocalDateTime.now().isAfter(user.getOtpExpiry())) {
            throw new RuntimeException("OTP has expired");
        }

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        user.setResetOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);
    }
}
