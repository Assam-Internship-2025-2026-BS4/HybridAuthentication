package in.bank.hdfc.auth.hybridAuth.repository;

import in.bank.hdfc.auth.hybridAuth.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByPhoneNo(String phoneNo);
    Optional<Customer> findByPhoneNoAndDob(String phoneNo, LocalDate dob);
    Optional<Customer> findByPhoneNoAndPan(String phoneNo, String pan);
}
