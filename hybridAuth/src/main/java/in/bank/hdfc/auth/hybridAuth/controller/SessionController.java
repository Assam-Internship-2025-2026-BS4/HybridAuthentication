package in.bank.hdfc.auth.hybridAuth.controller;

import in.bank.hdfc.auth.hybridAuth.dto.SessionFetchRequest;
import in.bank.hdfc.auth.hybridAuth.dto.SessionFetchResponse;
import in.bank.hdfc.auth.hybridAuth.dto.SessionInfo;
import in.bank.hdfc.auth.hybridAuth.entity.Customer;
import in.bank.hdfc.auth.hybridAuth.repository.CustomerRepository;
import in.bank.hdfc.auth.hybridAuth.repository.QrSessionRepository;
import in.bank.hdfc.auth.hybridAuth.service.JwtUtil;
import in.bank.hdfc.auth.hybridAuth.service.SessionLookupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/auth/session")
public class SessionController {

    private final SessionLookupService sessionLookupService;
    private final CustomerRepository   customerRepository;
    private final QrSessionRepository  qrSessionRepository;
    private final JwtUtil              jwtUtil;

    public SessionController(SessionLookupService sessionLookupService,
                              CustomerRepository customerRepository,
                              QrSessionRepository qrSessionRepository,
                              JwtUtil jwtUtil) {
        this.sessionLookupService = sessionLookupService;
        this.customerRepository   = customerRepository;
        this.qrSessionRepository  = qrSessionRepository;
        this.jwtUtil              = jwtUtil;
    }

        @PostMapping({"", "/fetch"})
    public ResponseEntity<SessionFetchResponse> fetchSession(
            @RequestBody SessionFetchRequest request) {

        if (request.getJourneyId() == null) {
            return ResponseEntity.badRequest()
                    .body(new SessionFetchResponse("FAILED", null, null,
                            "INVALID_REQUEST", 0, null, "MISSING_JOURNEY_ID",
                            "journeyId is required"));
        }

        SessionInfo info = sessionLookupService
                .findByJourneyId(request.getJourneyId()).orElse(null);
        if (info == null) {
            return ResponseEntity.status(404)
                    .body(new SessionFetchResponse("FAILED", null, null,
                            "SESSION_NOT_FOUND", 0, null, "SESSION_NOT_FOUND",
                            "Session not found"));
        }

        if (request.getSessionId() != null
                && !info.getSessionId().equals(request.getSessionId())) {
            return ResponseEntity.status(401)
                    .body(new SessionFetchResponse("FAILED", null, null,
                            "SESSION_MISMATCH", 0, null, "SESSION_MISMATCH",
                            "Session ID mismatch"));
        }

        if (info.getExpiresAt().isBefore(LocalDateTime.now())
                && !"Approved".equals(info.getAuthStatus())) {
            sessionLookupService.markExpired(request.getJourneyId());
            return ResponseEntity.ok(new SessionFetchResponse("SUCCESS",
                    info.getJourneyId(), info.getSessionId(),
                    "Expired", 0, null, null, "Session has expired"));
        }

        int    expiresIn = calcExpiresIn(info);

        String token = isApproved(info.getAuthStatus())
                ? generateToken(info) : null;

        return ResponseEntity.ok(new SessionFetchResponse("SUCCESS",
                info.getJourneyId(), info.getSessionId(),
                info.getAuthStatus(), expiresIn, token, null,
                "Session fetched successfully"));
    }

    @PostMapping("/approve")
    public ResponseEntity<SessionFetchResponse> approveSession(
            @RequestBody SessionFetchRequest request) {

        if (request.getJourneyId() == null || request.getSessionId() == null) {
            return ResponseEntity.badRequest()
                    .body(new SessionFetchResponse("FAILED", null, null,
                            "INVALID_REQUEST", 0, null, "MISSING_REQUIRED_FIELD",
                            "journeyId and sessionId are required"));
        }

        SessionInfo info = sessionLookupService
                .findByJourneyId(request.getJourneyId()).orElse(null);
        if (info == null) {
            return ResponseEntity.status(404)
                    .body(new SessionFetchResponse("FAILED", null, null,
                            "SESSION_NOT_FOUND", 0, null, "SESSION_NOT_FOUND",
                            "Session not found"));
        }

        if (!info.getSessionId().equals(request.getSessionId())) {
            return ResponseEntity.status(401)
                    .body(new SessionFetchResponse("FAILED", null, null,
                            "SESSION_MISMATCH", 0, null, "SESSION_MISMATCH",
                            "Session ID mismatch"));
        }

        if (info.getExpiresAt().isBefore(LocalDateTime.now())) {
            sessionLookupService.markExpired(request.getJourneyId());
            return ResponseEntity.ok(new SessionFetchResponse("SUCCESS",
                    info.getJourneyId(), info.getSessionId(),
                    "Expired", 0, null, null, "Session has expired"));
        }

        sessionLookupService.markApproved(request.getJourneyId(), 10);

        if (info.getMobileNo() != null && !info.getMobileNo().equals("Unknown")
                && !info.getMobileNo().equals("Pending")) {
            customerRepository.findByPhoneNo(info.getMobileNo()).ifPresent(c -> {
                c.setLastLoginTime(LocalDateTime.now());
                customerRepository.save(c);
            });
        }

        return ResponseEntity.ok(new SessionFetchResponse("SUCCESS",
                request.getJourneyId(), request.getSessionId(),
                "Approved", 0, null, null,
                "Session approved. Call /session/fetch to get accessToken."));
    }

    @PostMapping("/reject")
    public ResponseEntity<SessionFetchResponse> rejectSession(
            @RequestBody SessionFetchRequest request) {

        if (request.getJourneyId() == null || request.getSessionId() == null) {
            return ResponseEntity.badRequest()
                    .body(new SessionFetchResponse("FAILED", null, null,
                            "INVALID_REQUEST", 0, null, "MISSING_REQUIRED_FIELD",
                            "journeyId and sessionId are required"));
        }

        SessionInfo info = sessionLookupService
                .findByJourneyId(request.getJourneyId()).orElse(null);
        if (info == null) {
            return ResponseEntity.status(404)
                    .body(new SessionFetchResponse("FAILED", null, null,
                            "SESSION_NOT_FOUND", 0, null, "SESSION_NOT_FOUND",
                            "Session not found"));
        }

        if (!info.getSessionId().equals(request.getSessionId())) {
            return ResponseEntity.status(401)
                    .body(new SessionFetchResponse("FAILED", null, null,
                            "SESSION_MISMATCH", 0, null, "SESSION_MISMATCH",
                            "Session ID mismatch"));
        }

        // Only QR reject (WA rejection is handled by webhook)
        qrSessionRepository.findByJourneyId(request.getJourneyId()).ifPresent(s -> {
            s.setAuthStatus("Rejected");
            qrSessionRepository.save(s);
        });

        return ResponseEntity.ok(new SessionFetchResponse("SUCCESS",
                request.getJourneyId(), request.getSessionId(),
                "Rejected", 0, null, null, "Session rejected successfully"));
    }

    private String generateToken(SessionInfo info) {
        String mobileNo = info.getMobileNo();
        boolean hasRealMobile = mobileNo != null
                && !mobileNo.equals("Unknown")
                && !mobileNo.equals("Pending");

        if (!hasRealMobile) {
            mobileNo = "UNKNOWN";
        }

        Customer customer = hasRealMobile
                ? customerRepository.findByPhoneNo(info.getMobileNo()).orElse(null)
                : null;
        String customerId = customer != null ? customer.getCustomerId() : "UNKNOWN";
        Date expiry = Date.from(
                info.getExpiresAt().atZone(ZoneId.systemDefault()).toInstant());
        return jwtUtil.generateToken(
                mobileNo, customerId,
                info.getSessionId(), info.getAuthType(),
                info.getJourneyId(), expiry);
    }

    private int calcExpiresIn(SessionInfo info) {
        long ms = info.getExpiresAt().atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli() - System.currentTimeMillis();
        return (int) Math.max(0, ms / 1000);
    }

        private boolean isApproved(String authStatus) {
                return authStatus != null && "approved".equalsIgnoreCase(authStatus);
        }
}
