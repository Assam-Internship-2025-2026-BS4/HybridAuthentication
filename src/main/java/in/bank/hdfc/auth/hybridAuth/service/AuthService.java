package in.bank.hdfc.auth.hybridAuth.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Component;

import in.bank.hdfc.auth.hybridAuth.dto.AuthDataDTO;
import in.bank.hdfc.auth.hybridAuth.dto.JourneyHeader;
import in.bank.hdfc.auth.hybridAuth.dto.QRGenerateResponse;
import in.bank.hdfc.auth.hybridAuth.dto.QRHeader;
import in.bank.hdfc.auth.hybridAuth.dto.QrValidateResponse;
import in.bank.hdfc.auth.hybridAuth.dto.SessionDataDTO;
import in.bank.hdfc.auth.hybridAuth.entity.QRSession;
import in.bank.hdfc.auth.hybridAuth.enums.AuthType;
import in.bank.hdfc.auth.hybridAuth.repository.QRSessionRepository;
import in.bank.hdfc.auth.hybridAuth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AuthService {

        private final QRSessionRepository qrSessionRepository;
        private final JwtUtil jwtUtil;

        public AuthDataDTO initiateAuth(JourneyHeader journeyName) {

                String token = jwtUtil.generateToken();

                return new AuthDataDTO(
                                token,
                                "Bearer",
                                jwtUtil.getExpirationInSeconds());
        }

        // qr generate
        public QRGenerateResponse qrGenerate(QRHeader header) {

                long currentTime = System.currentTimeMillis();

                QRSession session = QRSession.builder()
                                .userAgent(header.getUserAgent())
                                .status("PENDING")
                                .createdAt(LocalDateTime.now())
                                .expiresIn(LocalDateTime.now().plusMinutes(1))
                                .authType(AuthType.QR)
                                .journeyId(UUID.randomUUID().toString())
                                .journeyName("LAS")
                                .build();

                qrSessionRepository.save(session);

                String deeplink = "http://your-mb-app-url/content/dam/hdfc-bank-cms/assets/corebanking/mb/nolang/v1/kavach/app_download.html?txn=nb-ln&ulid="
                                + session.getQrId() + "&ts=" + currentTime;

                // 🔥 Generate Base64 QR
                String base64Qr = jwtUtil.generateBase64Qr(deeplink);

                return new QRGenerateResponse(
                                session.getQrId(),
                                session.getCreatedAt(),
                                session.getExpiresIn(),
                                deeplink,
                                base64Qr
                );
        }

        public SessionDataDTO fetchSession(QRHeader header, String qrId) {

                QRSession session = qrSessionRepository
                                .findById(qrId)
                                .orElseThrow(() -> new RuntimeException("Session not found"));

                if (session.getExpiresIn().isBefore(LocalDateTime.now())) {
                        session.setStatus("EXPIRED");
                        qrSessionRepository.save(session);
                }

                return new SessionDataDTO(
                                "ETB",
                                true,
                                session.getStatus());
        }

        @Transactional
        public QrValidateResponse validateQR(QRHeader header, String qrId) {

                System.out.println("QR VALIDATE REQUEST FOR ID: " + qrId);

                QRSession session = qrSessionRepository
                                .findById(qrId)
                                .orElseThrow(() -> new RuntimeException("QR session not found"));

                System.out.println("Current status in DB: " + session.getStatus());

                if (session.getExpiresIn().isBefore(LocalDateTime.now())) {

                        System.out.println("SESSION EXPIRED");

                        session.setStatus("EXPIRED");
                        qrSessionRepository.saveAndFlush(session);

                        return new QrValidateResponse(qrId, "EXPIRED", false);
                }

                System.out.println("SETTING STATUS TO APPROVED");

                session.setStatus("APPROVED");
                qrSessionRepository.saveAndFlush(session);

                return new QrValidateResponse(qrId, "APPROVED", true);
        }

        // public QRGenerateResponse qrGenerate(QRHeader header, JourneyData data) {

        // QRSession session = qrSessionRepository.findByJourneyId(data.getJourneyId())
        // .orElseThrow(() -> new RuntimeException("Invalid Journey Id"));

        // if (!session.getJourneyName().equals(data.getJourneyName())) {
        // throw new RuntimeException("Journey Name mismatch");
        // }

        // if (session.getExpiresIn().isBefore(LocalDateTime.now())) {
        // throw new RuntimeException("Session Expired");
        // }

        // QRSession session1 = QRSession.builder()
        // .userAgent(header.getUserAgent())
        // .status("PENDING")
        // .createdAt(LocalDateTime.now())
        // .expiresIn(LocalDateTime.now().plusMinutes(1))
        // .authType(AuthType.QR)
        // .journeyId(UUID.randomUUID().toString())
        // .journeyName("LAS")
        // .build();

        // qrSessionRepository.save(session1);

        // String deeplink =
        // "http://your-mb-app-url/content/dam/hdfc-bank-cms/assets/corebanking/mb/nolang/v1/kavach/app_download.html?txn=nb-ln&ulid=1234&ts=1772004964615\n"
        // + //
        // "" + session.getQrId();

        // return new QRGenerateResponse(
        // session.getQrId(),
        // session.getCreatedAt(),
        // session.getExpiresIn(),
        // deeplink);

        // }
}
