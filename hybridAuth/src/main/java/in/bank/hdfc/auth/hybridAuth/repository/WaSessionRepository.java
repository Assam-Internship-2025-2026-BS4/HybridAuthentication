package in.bank.hdfc.auth.hybridAuth.repository;

import in.bank.hdfc.auth.hybridAuth.entity.WaSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface WaSessionRepository extends JpaRepository<WaSession, String> {
    Optional<WaSession> findByJourneyId(String journeyId);
    Optional<WaSession> findByJourneyIdAndMobileNo(String journeyId, String mobileNo);
    Optional<WaSession> findByMobileNoAndAuthStatus(String mobileNo, String authStatus);
    List<WaSession> findByExpiresAtBeforeAndAuthStatusNotIn(
            LocalDateTime instant, Collection<String> statuses);
}
