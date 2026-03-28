package in.bank.hdfc.auth.hybridAuth.service;

import in.bank.hdfc.auth.hybridAuth.entity.OtpSession;
import in.bank.hdfc.auth.hybridAuth.repository.OtpSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class OtpService {

    private static final Logger log = LoggerFactory.getLogger(OtpService.class);

    @Value("${otp.expiry-seconds:300}")
    private int expirySeconds;

    @Value("${otp.max-attempts:3}")
    private int maxAttempts;

    private final OtpSessionRepository otpSessionRepository;

    public OtpService(OtpSessionRepository otpSessionRepository) {
        this.otpSessionRepository = otpSessionRepository;
    }

    public void saveOtp(OtpSession session) {
        String otp = String.format("%06d", new SecureRandom().nextInt(1_000_000));
        session.setOtpCode(otp);
        session.setAttempt(0);
        session.setOtpExpiresAt(LocalDateTime.now().plusSeconds(expirySeconds));
        otpSessionRepository.save(session);

        log.info("OTP generated for {} -> {}",
            "******" + session.getMobileNo().substring(
                Math.max(0, session.getMobileNo().length() - 4)), otp);
    }

    public enum VerifyResult { SUCCESS, INVALID, EXPIRED, LOCKED }

    public VerifyResult verifyOtp(OtpSession session, String submitted) {
        if (session.getOtpCode() == null
                || session.getOtpCode().equals("USED")
                || session.getOtpCode().equals("EXPIRED")
                || session.getOtpCode().equals("PENDING")) return VerifyResult.INVALID;

        if (session.getAttempt() >= maxAttempts) return VerifyResult.LOCKED;

        if (LocalDateTime.now().isAfter(session.getOtpExpiresAt())) {
            session.setOtpCode("EXPIRED");
            otpSessionRepository.saveAndFlush(session);
            return VerifyResult.EXPIRED;
        }

        if (!session.getOtpCode().equals(submitted)) {
            session.setAttempt(session.getAttempt() + 1);
            otpSessionRepository.saveAndFlush(session);
            return VerifyResult.INVALID;
        }

        session.setOtpCode("USED");
        otpSessionRepository.saveAndFlush(session);
        return VerifyResult.SUCCESS;
    }
}
