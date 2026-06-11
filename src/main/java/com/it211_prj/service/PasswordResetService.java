package com.it211_prj.service;

import com.it211_prj.dto.auth.ChangePasswordRequest;
import com.it211_prj.dto.auth.ForgotPasswordRequest;
import com.it211_prj.dto.auth.ResetPasswordRequest;
import com.it211_prj.entity.PasswordResetToken;
import com.it211_prj.entity.User;
import com.it211_prj.exception.BadRequestException;
import com.it211_prj.exception.ResourceNotFoundException;
import com.it211_prj.repository.PasswordResetTokenRepository;
import com.it211_prj.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Value("${app.password-reset.expiration-ms:900000}")
    private long resetTokenExpirationMs;

    @Value("${app.password-reset.base-url:http://localhost:8080}")
    private String resetBaseUrl;

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User user = currentUserService.getCurrentUser();
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }
        if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
            throw new BadRequestException("New password must be different from current password");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        PasswordResetToken resetToken = tokenRepository.save(PasswordResetToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(Instant.now().plusMillis(resetTokenExpirationMs))
                .build());
        sendResetToken(user.getEmail(), resetToken.getToken());
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = tokenRepository.findByToken(request.token())
                .orElseThrow(() -> new BadRequestException("Invalid reset token"));
        if (resetToken.isUsed() || resetToken.getExpiresAt().isBefore(Instant.now())) {
            throw new BadRequestException("Reset token expired or already used");
        }
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        resetToken.setUsed(true);
        userRepository.save(user);
        tokenRepository.save(resetToken);
    }

    private void sendResetToken(String email, String token) {
        String link = resetBaseUrl + "/reset-password?token=" + token;
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("IT211 password reset");
            message.setText("Dung token nay de dat lai mat khau: " + link);
            mailSender.send(message);
        } catch (MailException ex) {
            log.info("[PASSWORD_RESET] SMTP khong kha dung. Dev dat lai token for {}: {}", email, token);
        }
    }
}
