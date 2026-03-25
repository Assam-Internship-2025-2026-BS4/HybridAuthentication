package in.bank.hdfc.auth.hybridAuth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.bank.hdfc.auth.hybridAuth.entity.WhatsappDevice;

import java.util.Optional;

public interface WhatsappDeviceRepository extends JpaRepository<WhatsappDevice, String> {

    Optional<WhatsappDevice> findByDeviceToken(String deviceToken);

    Optional<WhatsappDevice> findByCustomerId(String customerId);
}