package in.bank.hdfc.auth.hybridAuth.service;

import in.bank.hdfc.auth.hybridAuth.dto.SessionInfo;
import in.bank.hdfc.auth.hybridAuth.entity.OtpSession;
import in.bank.hdfc.auth.hybridAuth.entity.QrSession;
import in.bank.hdfc.auth.hybridAuth.entity.WaSession;
import in.bank.hdfc.auth.hybridAuth.repository.OtpSessionRepository;
import in.bank.hdfc.auth.hybridAuth.repository.QrSessionRepository;
import in.bank.hdfc.auth.hybridAuth.repository.WaSessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class SessionLookupService {

    private final OtpSessionRepository otpRepo;
    private final QrSessionRepository  qrRepo;
    private final WaSessionRepository  waRepo;

    public SessionLookupService(OtpSessionRepository otpRepo,
                                 QrSessionRepository qrRepo,
                                 WaSessionRepository waRepo) {
        this.otpRepo = otpRepo;
        this.qrRepo  = qrRepo;
        this.waRepo  = waRepo;
    }

    public Optional<SessionInfo> findByJourneyId(String journeyId) {
        Optional<OtpSession> otp = otpRepo.findByJourneyId(journeyId);
        if (otp.isPresent()) return Optional.of(SessionInfo.from(otp.get()));

        Optional<QrSession> qr = qrRepo.findByJourneyId(journeyId);
        if (qr.isPresent()) return Optional.of(SessionInfo.from(qr.get()));

        Optional<WaSession> wa = waRepo.findByJourneyId(journeyId);
        if (wa.isPresent()) return Optional.of(SessionInfo.from(wa.get()));

        return Optional.empty();
    }

    public void markApproved(String journeyId, int ttlMinutes) {
        LocalDateTime newExpiry = LocalDateTime.now().plusMinutes(ttlMinutes);

        otpRepo.findByJourneyId(journeyId).ifPresent(s -> {
            s.setAuthStatus("Approved");
            s.setExpiresAt(newExpiry);
            otpRepo.save(s);
        });
        qrRepo.findByJourneyId(journeyId).ifPresent(s -> {
            s.setAuthStatus("Approved");
            s.setExpiresAt(newExpiry);
            qrRepo.save(s);
        });
        waRepo.findByJourneyId(journeyId).ifPresent(s -> {
            s.setAuthStatus("Approved");
            s.setExpiresAt(newExpiry);
            waRepo.save(s);
        });
    }

    public void markExpired(String journeyId) {
        otpRepo.findByJourneyId(journeyId).ifPresent(s -> {
            s.setAuthStatus("Expired");
            s.setOtpCode("EXPIRED");
            otpRepo.save(s);
        });
        qrRepo.findByJourneyId(journeyId).ifPresent(s -> {
            s.setAuthStatus("Expired");
            qrRepo.save(s);
        });
        waRepo.findByJourneyId(journeyId).ifPresent(s -> {
            s.setAuthStatus("Expired");
            waRepo.save(s);
        });
    }

    public void expireAll() {
        LocalDateTime now      = LocalDateTime.now();
        List<String> terminal  = Arrays.asList("Expired", "Approved", "Rejected", "Invalidated");

        List<OtpSession> otpExp = otpRepo.findByExpiresAtBeforeAndAuthStatusNotIn(now, terminal);
        otpExp.forEach(s -> { s.setAuthStatus("Expired"); s.setOtpCode("EXPIRED"); s.setAttempt(0); });
        if (!otpExp.isEmpty()) otpRepo.saveAll(otpExp);

        List<QrSession> qrExp = qrRepo.findByExpiresAtBeforeAndAuthStatusNotIn(now, terminal);
        qrExp.forEach(s -> s.setAuthStatus("Expired"));
        if (!qrExp.isEmpty()) qrRepo.saveAll(qrExp);

        List<WaSession> waExp = waRepo.findByExpiresAtBeforeAndAuthStatusNotIn(now, terminal);
        waExp.forEach(s -> s.setAuthStatus("Expired"));
        if (!waExp.isEmpty()) waRepo.saveAll(waExp);
    }
}
