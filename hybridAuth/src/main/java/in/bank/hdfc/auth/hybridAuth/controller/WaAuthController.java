package in.bank.hdfc.auth.hybridAuth.controller;

import in.bank.hdfc.auth.hybridAuth.config.RateLimitConfig;
import in.bank.hdfc.auth.hybridAuth.dto.WaInitRequest;
import in.bank.hdfc.auth.hybridAuth.dto.WaInitResponse;
import in.bank.hdfc.auth.hybridAuth.dto.WaWebhookRequest;
import in.bank.hdfc.auth.hybridAuth.dto.WaWebhookResponse;
import in.bank.hdfc.auth.hybridAuth.entity.WaSession;
import in.bank.hdfc.auth.hybridAuth.repository.WaSessionRepository;
import in.bank.hdfc.auth.hybridAuth.service.RateLimiterService;
import in.bank.hdfc.auth.hybridAuth.util.BrowserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/v1/auth/wa")
public class WaAuthController {

    private static final Logger log = LoggerFactory.getLogger(WaAuthController.class);

    private final WaSessionRepository waSessionRepository;
    private final RateLimiterService rateLimiterService;
    @Value("${wa.expiry-seconds:300}")
    private int waExpirySeconds;

    public WaAuthController(WaSessionRepository waSessionRepository,
                            RateLimiterService rateLimiterService) {
        this.waSessionRepository = waSessionRepository;
        this.rateLimiterService = rateLimiterService;
    }

    // POST /api/v1/auth/wa/init
    // Body: { journeyId, journeyName, mobileNo }
    // Sends WA message — user replies YES/NO on WhatsApp (handled by webhook in prod)
    // Desktop polls /session/fetch every 3s to check status
    @PostMapping("/init")
    public ResponseEntity<WaInitResponse> initWaAuth(
            @RequestBody WaInitRequest request,
            @RequestHeader(value = "User-Agent",  required = false) String userAgent,
            @RequestHeader(value = "X-Latitude",  required = false) Double latitude,
            @RequestHeader(value = "X-Longitude", required = false) Double longitude,
            HttpServletRequest httpRequest) {

        if (request.getJourneyId() == null || request.getMobileNo() == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            rateLimiterService.checkLimit(
                    "WA_INIT:" + request.getMobileNo(),
                    RateLimitConfig.WA_LIMIT,
                    RateLimitConfig.WA_WINDOW
            );
        } catch (RuntimeException ex) {
            return ResponseEntity.status(429).body(new WaInitResponse(
                    request.getJourneyId(),
                    null,
                    "FAILED",
                    maskMobile(request.getMobileNo()),
                    null,
                    null,
                    "Too many WhatsApp auth requests. Please try again later."
            ));
        }

        String ua    = (userAgent != null && !userAgent.isBlank()) ? userAgent : "Unknown";
        String jName = (request.getJourneyName() != null
                && !request.getJourneyName().isBlank())
                ? request.getJourneyName() : "Gold Loan";
        double lat = (latitude  != null) ? latitude  : 0.0;
        double lon = (longitude != null) ? longitude : 0.0;
        String ip  = getClientIp(httpRequest);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime waExpiresAt = now.plusSeconds(waExpirySeconds);

        WaSession session = waSessionRepository
            .findByJourneyId(request.getJourneyId())
            .orElse(new WaSession());
        session.setJourneyId(request.getJourneyId());
        session.setJourneyName(jName);
        session.setMobileNo(request.getMobileNo());
        session.setAuthType("WA_Auth");
        session.setAuthStatus("PENDING");
        session.setWaReply("PENDING");
        session.setWaMessageId("WA-" + request.getMobileNo() + "-" + System.currentTimeMillis());
        session.setTokenType("Bearer");
        session.setUserAgent(ua);
        session.setBrowserUsed(BrowserUtil.detect(ua));
        session.setDeviceLatitude(lat);
        session.setDeviceLongitude(lon);
        session.setIpAddress(ip);
        if (session.getCreatedAt() == null) {
            session.setCreatedAt(now);
        }
        session.setWaMessageSentAt(now);
        session.setWaExpiresAt(waExpiresAt);  // Message expires in 5 minutes
        session.setExpiresAt(now.plusSeconds(1799));  // Session expires in ~30 minutes
        waSessionRepository.save(session);

        log.info("WA auth initiated for {}", maskMobile(request.getMobileNo()));
        // TODO: In prod — call WhatsApp Business API to send message here

        DateTimeFormatter fmt = DateTimeFormatter.ISO_INSTANT;
        String createdAtStr = now.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC")).format(fmt);
        String expiresAtStr = waExpiresAt.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC")).format(fmt);

        return ResponseEntity.ok(new WaInitResponse(
                request.getJourneyId(),
                session.getWaSessionId(),
                "PENDING",
                maskMobile(request.getMobileNo()),
                createdAtStr,
                expiresAtStr,
                "A WhatsApp message has been sent. Reply YES to approve login."));
    }

    // POST /api/v1/auth/wa/webhook
    // Called by WhatsApp Business API when user replies YES/NO
    // Body: { journeyId, waReply: "YES" or "NO" }
    @PostMapping("/webhook")
    public ResponseEntity<WaWebhookResponse> handleWaWebhook(
            @RequestBody WaWebhookRequest request) {

        if (request.getJourneyId() == null || request.getWaReply() == null) {
            return ResponseEntity.badRequest().body(new WaWebhookResponse(
                    "FAILED", null, null, null, "journeyId and waReply are required"));
        }

        WaSession session = waSessionRepository
                .findByJourneyId(request.getJourneyId())
                .orElse(null);

        if (session == null) {
            return ResponseEntity.status(404).body(new WaWebhookResponse(
                    "FAILED", request.getJourneyId(), null, null, "Session not found"));
        }

        String waReply = request.getWaReply().toUpperCase();
        if (!waReply.equals("YES") && !waReply.equals("NO")) {
            return ResponseEntity.badRequest().body(new WaWebhookResponse(
                    "FAILED", request.getJourneyId(), session.getAuthStatus(), null,
                    "waReply must be YES or NO"));
        }

        LocalDateTime now = LocalDateTime.now();

        // Update session based on reply
        session.setWaReply(waReply);
        session.setWaReplyReceivedAt(now);

        if ("YES".equals(waReply)) {
            session.setAuthStatus("Approved");
        } else {
            session.setAuthStatus("Rejected");
        }

        waSessionRepository.save(session);

        log.info("WA webhook received for journeyId={}, reply={}", request.getJourneyId(), waReply);

        return ResponseEntity.ok(new WaWebhookResponse(
                "SUCCESS",
                request.getJourneyId(),
                session.getAuthStatus(),
                waReply,
                "WhatsApp reply processed successfully"));
    }

    private String maskMobile(String mobile) {
        if (mobile == null || mobile.length() < 4) return "****";
        return "******" + mobile.substring(mobile.length() - 4);
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
