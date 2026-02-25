package in.bank.hdfc.auth.hybridAuth.service;

import in.bank.hdfc.auth.hybridAuth.entity.AuthSession;
import in.bank.hdfc.auth.hybridAuth.entity.AuthSessionStatus;
import in.bank.hdfc.auth.hybridAuth.exception.UserNotVerifiedException;
import in.bank.hdfc.auth.hybridAuth.repository.AuthSessionRepository;
import in.bank.hdfc.auth.hybridAuth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthQrService {

    private final AuthSessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Transactional
    public AuthSession generateQrSession() {

        String sessionToken = UUID.randomUUID().toString();
        String qrToken = UUID.randomUUID().toString();

        AuthSession session = new AuthSession(
                sessionToken,
                Instant.now().plusSeconds(120)
        );

        session.setQrToken(qrToken);
        sessionRepository.save(session);

        return session;
    }

    @Transactional
    public void validateQr(String qrToken, String mobile) {

        AuthSession session = sessionRepository.findByQrToken(qrToken)
                .orElseThrow(() ->
                        new RuntimeException("Invalid QR token"));

        if (session.getStatus() != AuthSessionStatus.PENDING) {
            return;
        }

        if (session.getExpiresAt().isBefore(Instant.now())) {
            session.setStatus(AuthSessionStatus.EXPIRED);
            return;
        }

        // defensive user validation
        if (!userRepository.existsByMobile(mobile)) {
            throw new UserNotVerifiedException("Invalid user");
        }

        session.setStatus(AuthSessionStatus.APPROVED);
        session.setApprovedAt(Instant.now());
        session.setQrToken(null); // invalidate QR

        sessionRepository.save(session);
    }
}