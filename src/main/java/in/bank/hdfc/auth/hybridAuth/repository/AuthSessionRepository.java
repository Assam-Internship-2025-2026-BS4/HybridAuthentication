package in.bank.hdfc.auth.hybridAuth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.bank.hdfc.auth.hybridAuth.entity.AuthSession;


public interface AuthSessionRepository extends JpaRepository<AuthSession, String>{
    
    Optional<AuthSession> findByQrToken(String qrToken);
}


