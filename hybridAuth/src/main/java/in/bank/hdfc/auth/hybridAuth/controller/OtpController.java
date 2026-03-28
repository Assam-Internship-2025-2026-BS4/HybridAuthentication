package in.bank.hdfc.auth.hybridAuth.controller;

import in.bank.hdfc.auth.hybridAuth.config.RateLimitConfig;
import in.bank.hdfc.auth.hybridAuth.dto.*;
import in.bank.hdfc.auth.hybridAuth.entity.Customer;
import in.bank.hdfc.auth.hybridAuth.entity.OtpSession;
import in.bank.hdfc.auth.hybridAuth.repository.CustomerRepository;
import in.bank.hdfc.auth.hybridAuth.repository.OtpSessionRepository;
import in.bank.hdfc.auth.hybridAuth.service.JwtUtil;
import in.bank.hdfc.auth.hybridAuth.service.OtpService;
import in.bank.hdfc.auth.hybridAuth.service.OtpService.VerifyResult;
import in.bank.hdfc.auth.hybridAuth.service.RateLimiterService;
import in.bank.hdfc.auth.hybridAuth.service.SessionLookupService;
import in.bank.hdfc.auth.hybridAuth.util.BrowserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth/otp")
public class OtpController {

    private final OtpService           otpService;
    private final OtpSessionRepository otpSessionRepository;
    private final CustomerRepository   customerRepository;
    private final SessionLookupService sessionLookupService;
    private final JwtUtil              jwtUtil;
    private final RateLimiterService   rateLimiterService;

        @Value("${otp.max-attempts:3}")
        private int otpMaxAttempts;

    public OtpController(OtpService otpService,
                         OtpSessionRepository otpSessionRepository,
                         CustomerRepository customerRepository,
                         SessionLookupService sessionLookupService,
                         JwtUtil jwtUtil,
                         RateLimiterService rateLimiterService) {
        this.otpService           = otpService;
        this.otpSessionRepository = otpSessionRepository;
        this.customerRepository   = customerRepository;
        this.sessionLookupService = sessionLookupService;
        this.jwtUtil              = jwtUtil;
        this.rateLimiterService   = rateLimiterService;
    }

   
    @PostMapping("/init")
    public ResponseEntity<OtpInitResponse> initOtp(
            @RequestBody OtpInitRequest request,
            @RequestHeader(value = "User-Agent",  required = false) String userAgent,
            @RequestHeader(value = "X-Latitude",  required = false) String latitude,
            @RequestHeader(value = "X-Longitude", required = false) String longitude,
            HttpServletRequest httpRequest) {

        if (request.getMobileNo() == null || request.getJourneyId() == null) {
            return ResponseEntity.badRequest()
                    .body(new OtpInitResponse("FAILED", null, null, null,
                            "mobileNo and journeyId are required"));
        }

        try {
    rateLimiterService.checkLimit(
            "OTP_INIT:" + request.getMobileNo(),
            RateLimitConfig.OTP_INIT_LIMIT,
            RateLimitConfig.OTP_INIT_WINDOW
    );
} catch (RuntimeException ex) {
    return ResponseEntity.status(429)
            .body(new OtpInitResponse("FAILED", null, null, null,
                    "Too many OTP requests. Please try again later."));
}

        Optional<Customer> customerOpt = customerRepository.findByPhoneNo(request.getMobileNo());
        if (customerOpt.isEmpty()) {
            // Don't reveal if number is unregistered — return dummy response
            return ResponseEntity.ok(new OtpInitResponse("SUCCESS",
                    request.getJourneyId(), UUID.randomUUID().toString(),
                    "300", "OTP sent if number is registered"));
        }

        // Invalidate any existing pending OTP for this mobile
        otpSessionRepository.findByMobileNoAndAuthStatus(request.getMobileNo(), "Pending")
            .ifPresent(existing -> {
                existing.setAuthStatus("Invalidated");
                existing.setOtpCode("USED");
                otpSessionRepository.save(existing);
            });

        // Parse headers — fallback to defaults so no nulls stored
        double lon = 0.0, lat = 0.0;
        try {
            if (longitude != null && !longitude.isBlank()) lon = Double.parseDouble(longitude);
            if (latitude  != null && !latitude.isBlank())  lat = Double.parseDouble(latitude);
        } catch (NumberFormatException ignored) {}

        String ua      = (userAgent != null && !userAgent.isBlank()) ? userAgent : "Unknown";
        String ip      = getClientIp(httpRequest);
        String jName   = (request.getJourneyName() != null && !request.getJourneyName().isBlank())
                          ? request.getJourneyName() : "Gold Loan";
        LocalDateTime now = LocalDateTime.now();

        OtpSession session = otpSessionRepository
                .findByJourneyId(request.getJourneyId())
                .orElse(new OtpSession());
        session.setJourneyId(request.getJourneyId());
        session.setJourneyName(jName);
        session.setMobileNo(request.getMobileNo());
        session.setAuthType("OTP");
        session.setAuthStatus("Pending");
        session.setOtpCode("PENDING");
        session.setAttempt(0);
        session.setTokenType("Bearer");
        session.setUserAgent(ua);
        session.setOtpSendAt(now);
        session.setBrowserUsed(BrowserUtil.detect(ua));
        session.setDeviceLatitude(lat);
        session.setDeviceLongitude(lon);
        session.setIpAddress(ip);
                if (session.getCreatedAt() == null) {
                        session.setCreatedAt(now);
                }
        session.setOtpExpiresAt(now.plusSeconds(300));   // OTP valid 5 min
        session.setExpiresAt(now.plusSeconds(1799));      // Session valid 30 min

        otpService.saveOtp(session);

        return ResponseEntity.ok(new OtpInitResponse("SUCCESS",
                request.getJourneyId(), session.getOtpSessionId(),
                "300", "OTP sent if number is registered"));
    }

    @PostMapping("/validate")
    public ResponseEntity<OtpValidateResponse> validateOtp(
            @RequestBody OtpValidateRequest request) {

        if (request.getMobileNo() == null || request.getJourneyId() == null
                || request.getOtpSessionId() == null || request.getOtp() == null) {
            return ResponseEntity.badRequest()
                    .body(new OtpValidateResponse("FAILED", null, null, null,
                            0, null, "MISSING_REQUIRED_FIELD", "Missing required fields"));
        }

        String maskedMobileNo = maskMobile(request.getMobileNo());

        try {
            rateLimiterService.checkLimit(
                    "OTP_VALIDATE:" + request.getMobileNo(),
                    RateLimitConfig.OTP_VALIDATE_LIMIT,
                    RateLimitConfig.OTP_VALIDATE_WINDOW
            );
        } catch (RuntimeException ex) {
            return ResponseEntity.status(429)
                    .body(new OtpValidateResponse("FAILED", request.getJourneyId(),
                            request.getOtpSessionId(), maskedMobileNo, null, 0, null,
                            "RATE_LIMIT_EXCEEDED",
                            "Too many OTP validation attempts. Please try again later."));
        }

        OtpSession session = otpSessionRepository
                .findByJourneyId(request.getJourneyId()).orElse(null);
        if (session == null || !session.getOtpSessionId().equals(request.getOtpSessionId())) {
            return ResponseEntity.status(404)
                    .body(new OtpValidateResponse("FAILED", null, null, null,
                            0, null, "SESSION_NOT_FOUND", "Session not found."));
        }

        VerifyResult result = otpService.verifyOtp(session, request.getOtp());

        if (result == VerifyResult.LOCKED) {
            return ResponseEntity.status(429)
                    .body(new OtpValidateResponse("FAILED", request.getJourneyId(),
                            request.getOtpSessionId(), maskedMobileNo, null, 0, 0,
                            "OTP_MAX_ATTEMPTS_EXCEEDED",
                            "Too many incorrect attempts. Please request a new OTP."));
        }
        if (result == VerifyResult.EXPIRED) {
            return ResponseEntity.status(401)
                    .body(new OtpValidateResponse("FAILED", request.getJourneyId(),
                            request.getOtpSessionId(), maskedMobileNo, null, 0, null,
                            "OTP_EXPIRED", "OTP has expired. Please request a new one."));
        }
        if (result != VerifyResult.SUCCESS) {
                        int remaining = Math.max(0, otpMaxAttempts - session.getAttempt());
            return ResponseEntity.status(422)
                    .body(new OtpValidateResponse("FAILED", request.getJourneyId(),
                            request.getOtpSessionId(), maskedMobileNo, null, remaining, null,
                            "OTP_INVALID", "Invalid OTP."));
        }

        // Approve session
        sessionLookupService.markApproved(request.getJourneyId(), 10);

        // Update lastLoginTime
        customerRepository.findByPhoneNo(request.getMobileNo()).ifPresent(c -> {
            c.setLastLoginTime(LocalDateTime.now());
            customerRepository.save(c);
        });

        // Re-fetch for updated expiresAt
        OtpSession fresh = otpSessionRepository
                .findByJourneyId(request.getJourneyId()).orElse(session);

        Customer customer = customerRepository
                .findByPhoneNo(request.getMobileNo()).orElse(null);
        String customerId = customer != null ? customer.getCustomerId() : "UNKNOWN";
        Date expiry = Date.from(
                fresh.getExpiresAt().atZone(ZoneId.systemDefault()).toInstant());

        String token = jwtUtil.generateToken(
                request.getMobileNo(), customerId,
                fresh.getOtpSessionId(), "OTP",
                fresh.getJourneyId(), expiry);

        int expiresIn = (int) Math.max(0,
                (fresh.getExpiresAt().atZone(ZoneId.systemDefault())
                        .toInstant().toEpochMilli() - System.currentTimeMillis()) / 1000);

        return ResponseEntity.ok(new OtpValidateResponse("SUCCESS",
                fresh.getJourneyId(), fresh.getOtpSessionId(),
                                maskedMobileNo, token, expiresIn, null, null,
                                "Authentication successful."));
    }

        private String maskMobile(String mobileNo) {
                if (mobileNo == null || mobileNo.isBlank()) return "Unknown";
                if (mobileNo.equals("Unknown") || mobileNo.equals("Pending")) return mobileNo;
                if (mobileNo.length() <= 4) return "****";
                return "******" + mobileNo.substring(mobileNo.length() - 4);
        }

    private String getClientIp(HttpServletRequest req) {
        String ip = req.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) return ip.split(",")[0].trim();
        ip = req.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty()) return ip;
        String remote = req.getRemoteAddr();
        return (remote != null && !remote.isBlank()) ? remote : "0.0.0.0";
    }
}
