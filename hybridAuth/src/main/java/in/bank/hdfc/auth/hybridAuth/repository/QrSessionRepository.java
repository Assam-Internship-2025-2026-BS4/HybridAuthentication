package in.bank.hdfc.auth.hybridAuth.repository;

import in.bank.hdfc.auth.hybridAuth.entity.QrSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface QrSessionRepository extends JpaRepository<QrSession, String> {
    Optional<QrSession> findByJourneyId(String journeyId);
    Optional<QrSession> findByQrCodeId(String qrCodeId);
    List<QrSession> findByExpiresAtBeforeAndAuthStatusNotIn(
            LocalDateTime instant, Collection<String> statuses);
}
