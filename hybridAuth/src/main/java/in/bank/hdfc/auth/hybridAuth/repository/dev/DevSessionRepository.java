package in.bank.hdfc.auth.hybridAuth.repository.dev;

import in.bank.hdfc.auth.hybridAuth.entity.dev.DevEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DevSessionRepository extends JpaRepository<DevEntity ,Long> {

    Optional<DevEntity> findById(Long id);

}
