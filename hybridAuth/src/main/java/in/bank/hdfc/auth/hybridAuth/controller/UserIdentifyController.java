package in.bank.hdfc.auth.hybridAuth.controller;

import in.bank.hdfc.auth.hybridAuth.dto.UserIdentifyRequest;
import in.bank.hdfc.auth.hybridAuth.dto.UserIdentifyResponse;
import in.bank.hdfc.auth.hybridAuth.entity.Customer;
import in.bank.hdfc.auth.hybridAuth.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user/details")
public class UserIdentifyController {

    private final CustomerRepository customerRepository;
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public UserIdentifyController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // POST /api/v1/user/details/identify
    // Body: { mobileNo, pan } or { mobileNo, dob }
    // Returns: { status, identified, hasWhatsAppEnabled }
    @PostMapping("/identify")
    public ResponseEntity<UserIdentifyResponse> identifyUser(
            @RequestBody UserIdentifyRequest request) {

        if (!request.isValid()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Customer> customerOpt = Optional.empty();

        if (request.getPan() != null && !request.getPan().isEmpty()) {
            customerOpt = customerRepository.findByPhoneNoAndPan(
                    request.getMobileNo(), request.getPan());
        } else if (request.getDob() != null && !request.getDob().isEmpty()) {
            try {
                LocalDate dob = LocalDate.parse(request.getDob(), DATE_FORMATTER);
                customerOpt = customerRepository.findByPhoneNoAndDob(
                        request.getMobileNo(), dob);
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        }

        // Don't reveal if number exists — always return identified=true for registered users
        boolean waEnabled = customerOpt
                .map(c -> Boolean.TRUE.equals(c.getWhatsappEnabled()))
                .orElse(false);
        boolean found = customerOpt.isPresent();

        return ResponseEntity.ok(
                new UserIdentifyResponse("SUCCESS", found, waEnabled));
    }
}
