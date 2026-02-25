package in.bank.hdfc.auth.hybridAuth.repository;

import in.bank.hdfc.auth.hybridAuth.entity.AuthSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthSessionRepository extends JpaRepository<AuthSession, UUID> {
    Optional<AuthSession> findBySessionToken(String sessionToken);
    Optional<AuthSession> findByQrToken(String qrToken);
}
