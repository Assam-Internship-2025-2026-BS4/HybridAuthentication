package in.bank.hdfc.auth.hybridAuth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import in.bank.hdfc.auth.hybridAuth.client.SmsClient;
import in.bank.hdfc.auth.hybridAuth.client.WhatsAppClient;
import in.bank.hdfc.auth.hybridAuth.dto.AuthInitResponse;
import in.bank.hdfc.auth.hybridAuth.dto.OtpValidateResponse;
import in.bank.hdfc.auth.hybridAuth.dto.QrValidateResponse;
import in.bank.hdfc.auth.hybridAuth.dto.SessionFetchResponse;
import in.bank.hdfc.auth.hybridAuth.entity.AuthSession;
import in.bank.hdfc.auth.hybridAuth.enums.AuthType;
import in.bank.hdfc.auth.hybridAuth.repository.AuthSessionRepository;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthSessionRepository repository;
    private final SmsClient smsClient;
    private final WhatsAppClient whatsAppClient;

    public AuthInitResponse initiateAuth(String mobile, AuthType authType) {

        AuthSession session = AuthSession.builder()
                .mobileNumber(mobile)
                .authType(authType)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(1))
                .build();

        repository.save(session);

        switch (authType) {
            case OTP -> handleOtp(mobile, session);
            case WA -> handleWhatsApp(mobile, session);
            case QR -> generateQrSession(mobile, session);
        }

        return new AuthInitResponse(
                session.getSessionId(),
                session.getStatus());
    }

    // handle otp session creation 
    private void handleOtp(String mobile, AuthSession session) {

        String otp = String.valueOf(
                ThreadLocalRandom.current().nextInt(100000, 999999));

        session.setOtpHash(otp);
        repository.save(session);

        smsClient.sendOtp(mobile, otp);
    }

    // handle whatsapp session creation
    private void handleWhatsApp(String mobile, AuthSession session) {

        whatsAppClient.sendAuthPush(
                mobile,
                session.getSessionId());
    }

    // generate QR session
    private void generateQrSession(String mobile, AuthSession session) {

        String qrToken = UUID.randomUUID().toString();

        session.setQrToken(qrToken);

        repository.save(session);

    }

    public OtpValidateResponse validateOtp(String sessionId, String otp) {

        AuthSession session = repository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        // Check auth type
        if (!AuthType.OTP.equals(session.getAuthType())) {
            throw new RuntimeException("Invalid auth type");
        }

        // Check expiry
        // if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
        //     session.setStatus("EXPIRED");
        //     repository.save(session);

        //     return new OtpValidateResponse(
        //             session.getSessionId(),
        //             "EXPIRED",
        //             false);
        // }

        // Check OTP match
        if (session.getOtpHash() != null &&
                session.getOtpHash().equals(otp)) {

            session.setStatus("APPROVED");
            repository.save(session);

            return new OtpValidateResponse(
                    session.getSessionId(),
                    "APPROVED",
                    true);
        }

        // Wrong OTP
        session.setStatus("REJECTED");
        repository.save(session);

        return new OtpValidateResponse(
                session.getSessionId(),
                "REJECTED",
                false);
    }

    // validate QR
    public QrValidateResponse validateQr(String qrToken) {

        AuthSession session = repository.findByQrToken(qrToken)
                .orElseThrow(() -> new RuntimeException("Invalid QR"));

        if (!AuthType.QR.equals(session.getAuthType())) {
            throw new RuntimeException("Invalid auth type");
        }

        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            session.setStatus("EXPIRED");
            repository.save(session);

            return new QrValidateResponse(
                    session.getSessionId(),
                    "EXPIRED",
                    false);
        }

        return new QrValidateResponse(
                session.getSessionId(),
                session.getStatus(),
                true);
    }

    // update session status
    public void updateSessionStatus(String sessionId, String action) {

        AuthSession session = repository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if ("APPROVED".equals(action)) {
            session.setStatus("APPROVED");
        } else {
            session.setStatus("REJECTED");
        }

        repository.save(session);
    }

    // reject session
    public void rejectSession(String sessionId, String action){
        AuthSession session = repository.findById(sessionId)
        .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setStatus("REJECTED");

        repository.save(session);

    }

    // fetch session
    public SessionFetchResponse fetchSession(String sessionId) {

        AuthSession session = repository
                .findById(sessionId)
                .orElseThrow();

        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            session.setStatus("EXPIRED");
            repository.save(session);
        }

        return new SessionFetchResponse(session.getStatus());
    }
}
