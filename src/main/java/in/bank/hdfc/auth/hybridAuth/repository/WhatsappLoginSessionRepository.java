package in.bank.hdfc.auth.hybridAuth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.bank.hdfc.auth.hybridAuth.entity.WhatsappLoginSession;

public interface WhatsappLoginSessionRepository extends JpaRepository<WhatsappLoginSession, String> {
}