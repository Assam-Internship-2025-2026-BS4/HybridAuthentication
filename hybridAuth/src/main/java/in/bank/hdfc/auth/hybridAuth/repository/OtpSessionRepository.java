package in.bank.hdfc.auth.hybridAuth.repository;

import in.bank.hdfc.auth.hybridAuth.entity.OtpSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpSessionRepository
        extends JpaRepository<OtpSession, Long> {

    Optional<OtpSession>
    findTopByMobileOrderByExpiresAtDesc(String mobile);
}