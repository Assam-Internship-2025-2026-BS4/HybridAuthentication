package in.bank.hdfc.auth.hybridAuth.controller;

import in.bank.hdfc.auth.hybridAuth.dto.SessionInfo;
import in.bank.hdfc.auth.hybridAuth.dto.UserDetailsFetchResponse;
import in.bank.hdfc.auth.hybridAuth.entity.Customer;
import in.bank.hdfc.auth.hybridAuth.repository.CustomerRepository;
import in.bank.hdfc.auth.hybridAuth.service.JwtUtil;
import in.bank.hdfc.auth.hybridAuth.service.SessionLookupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
public class UserDetailsController {

    private final CustomerRepository   customerRepository;
    private final SessionLookupService sessionLookupService;
    private final JwtUtil              jwtUtil;

    public UserDetailsController(CustomerRepository customerRepository,
                                  SessionLookupService sessionLookupService,
                                  JwtUtil jwtUtil) {
        this.customerRepository   = customerRepository;
        this.sessionLookupService = sessionLookupService;
        this.jwtUtil              = jwtUtil;
    }

    // POST /api/v1/user/details/fetch
    // No body — everything extracted from JWT
    // Header: Authorization: Bearer <token>
    @PostMapping("/api/v1/user/details/fetch")
    public ResponseEntity<UserDetailsFetchResponse> fetchUserDetails(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                    .body(new UserDetailsFetchResponse("FAILED", null, null,
                            null, null, null, null, "TOKEN_MISSING"));
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401)
                    .body(new UserDetailsFetchResponse("FAILED", null, null,
                            null, null, null, null, "TOKEN_INVALID"));
        }

        // Extract all claims from JWT — no body needed
        String mobileNo   = jwtUtil.getMobileNoFromToken(token);
        String sessionId  = jwtUtil.getSessionIdFromToken(token);
        String journeyId  = jwtUtil.getJourneyIdFromToken(token);

        if (mobileNo == null || sessionId == null || journeyId == null) {
            return ResponseEntity.status(401)
                    .body(new UserDetailsFetchResponse("FAILED", null, null,
                            null, null, null, null, "TOKEN_CLAIMS_MISSING"));
        }

        // Verify session is still Approved
        SessionInfo info = sessionLookupService
                .findByJourneyId(journeyId).orElse(null);
        if (info == null || !"Approved".equals(info.getAuthStatus())) {
            return ResponseEntity.status(401)
                    .body(new UserDetailsFetchResponse("FAILED", null, null,
                            null, null, null, null, "SESSION_NOT_APPROVED"));
        }

        Optional<Customer> userOpt = customerRepository.findByPhoneNo(mobileNo);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new UserDetailsFetchResponse("FAILED", null, null,
                            null, null, null, null, "CUSTOMER_NOT_FOUND"));
        }

        Customer user         = userOpt.get();
        String lastLoginTime  = Instant.now()
                .atZone(ZoneId.of("UTC"))
                .format(DateTimeFormatter.ISO_INSTANT);

        return ResponseEntity.ok(new UserDetailsFetchResponse(
                "SUCCESS",
                user.getCustomerId(),
                user.getCustomerName(),
                mobileNo,
                lastLoginTime,
                journeyId,
                sessionId,
                null));
    }
}
