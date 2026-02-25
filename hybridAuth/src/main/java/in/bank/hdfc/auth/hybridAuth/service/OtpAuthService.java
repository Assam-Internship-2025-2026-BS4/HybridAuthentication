package in.bank.hdfc.auth.hybridAuth.service;

import in.bank.hdfc.auth.hybridAuth.entity.AuthSession;
import in.bank.hdfc.auth.hybridAuth.entity.AuthSessionStatus;
import in.bank.hdfc.auth.hybridAuth.entity.OtpSession;
import in.bank.hdfc.auth.hybridAuth.repository.AuthSessionRepository;
import in.bank.hdfc.auth.hybridAuth.repository.OtpSessionRepository;
import in.bank.hdfc.auth.hybridAuth.util.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class OtpAuthService {

    private final OtpSessionRepository otpRepo;
    private final AuthSessionRepository authRepo;

    @Transactional
    public AuthSession initOtp(String mobile) {

        // 1️⃣ Create AuthSession FIRST
        AuthSession authSession = new AuthSession(
                UUID.randomUUID().toString(),
                Instant.now().plusSeconds(180)
        );
        authRepo.save(authSession);

        // 2️⃣ Generate OTP
        String otp = OtpUtil.generateOtp();

        OtpSession otpSession = new OtpSession();
        otpSession.setMobile(mobile);
        otpSession.setOtpHash(OtpUtil.hashOtp(otp));
        otpSession.setExpiresAt(Instant.now().plusSeconds(120));
        otpSession.setAttempts(0);
        otpSession.setAuthSession(authSession); // 🔗 LINK

        otpRepo.save(otpSession);

        // simulate SMS
        System.out.println("OTP sent to " + mobile + ": " + otp);

        return authSession;
    }

    @Transactional
    public void verifyOtp(String mobile, String otp) {

        OtpSession otpSession =
                otpRepo.findTopByMobileOrderByExpiresAtDesc(mobile)
                        .orElseThrow(() ->
                                new RuntimeException("OTP not found"));

        if (otpSession.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("OTP expired");
        }

        otpSession.setAttempts(otpSession.getAttempts() + 1);

        if (otpSession.getAttempts() > 3) {
            throw new RuntimeException("OTP attempts exceeded");
        }

        String hashedOtp = OtpUtil.hashOtp(otp);

        if (!hashedOtp.equals(otpSession.getOtpHash())) {
            otpRepo.save(otpSession);
            throw new RuntimeException("Invalid OTP");
        }

        // ✅ Approve ONLY the linked session
        AuthSession session = otpSession.getAuthSession();
        session.setStatus(AuthSessionStatus.APPROVED);
        session.setApprovedAt(Instant.now());

        authRepo.save(session);
    }
}