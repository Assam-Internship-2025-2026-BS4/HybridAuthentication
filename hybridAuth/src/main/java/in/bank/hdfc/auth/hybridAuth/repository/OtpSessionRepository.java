package in.bank.hdfc.auth.hybridAuth.repository;

import in.bank.hdfc.auth.hybridAuth.entity.OtpSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface OtpSessionRepository extends JpaRepository<OtpSession, String> {
    Optional<OtpSession> findByJourneyId(String journeyId);
    Optional<OtpSession> findByMobileNoAndAuthStatus(String mobileNo, String authStatus);
    List<OtpSession> findByExpiresAtBeforeAndAuthStatusNotIn(
            LocalDateTime instant, Collection<String> statuses);
}
