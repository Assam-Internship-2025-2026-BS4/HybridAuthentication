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
public class AuthSessionService {

    private final AuthSessionRepository repository;
    private final UserRepository userRepository;

    @Transactional
    public AuthSession fetchSession(String sessionToken) {
        AuthSession session = repository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.getStatus() == AuthSessionStatus.PENDING
                && session.getExpiresAt().isBefore(Instant.now())) {

            session.setStatus(AuthSessionStatus.EXPIRED);
            repository.save(session);
        }

        return session;
    }


    @Transactional
    public void approveSession(String sessionToken, boolean approved) {
        AuthSession session = repository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.getStatus() != AuthSessionStatus.PENDING) {
            return ;
        }

        session.setStatus(
                approved
                        ? AuthSessionStatus.APPROVED
                        : AuthSessionStatus.REJECTED
        );

        session.setApprovedAt(Instant.now());
        repository.save(session);
    }

    @Transactional
    public AuthSession initWhatsAppSession(String mobile) {
        // 🔐 DEFENSIVE BACKEND CHECK
        if (!userRepository.existsByMobile(mobile)) {
            throw new UserNotVerifiedException("User is not verified");
        }

        String sessionToken = UUID.randomUUID().toString();

        Instant expiresAt = Instant.now().plusSeconds(60);

        AuthSession session = new AuthSession(sessionToken, expiresAt);

        repository.save(session);

        return session;
    }

}
