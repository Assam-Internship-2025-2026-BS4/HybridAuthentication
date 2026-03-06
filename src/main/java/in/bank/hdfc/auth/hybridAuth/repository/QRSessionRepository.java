package in.bank.hdfc.auth.hybridAuth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.bank.hdfc.auth.hybridAuth.entity.QRSession;

public interface QRSessionRepository extends JpaRepository<QRSession, String> {
    Optional<QRSession> findByJourneyId(String journeyId);
}
