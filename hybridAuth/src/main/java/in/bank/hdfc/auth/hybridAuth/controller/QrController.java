package in.bank.hdfc.auth.hybridAuth.controller;

import in.bank.hdfc.auth.hybridAuth.config.RateLimitConfig;
import in.bank.hdfc.auth.hybridAuth.dto.*;
import in.bank.hdfc.auth.hybridAuth.entity.QrSession;
import in.bank.hdfc.auth.hybridAuth.repository.QrSessionRepository;
import in.bank.hdfc.auth.hybridAuth.service.RateLimiterService;
import in.bank.hdfc.auth.hybridAuth.util.BrowserUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@RestController
public class QrController {

    private final QrSessionRepository qrSessionRepository;
    private final RateLimiterService rateLimiterService;

    public QrController(QrSessionRepository qrSessionRepository,
                        RateLimiterService rateLimiterService) {
        this.qrSessionRepository = qrSessionRepository;
        this.rateLimiterService = rateLimiterService;
    }

    
    @PostMapping("/api/v1/auth/qr/generate")
    public ResponseEntity<?> generateQr(
            @RequestBody(required = false) QrGenerateRequest request,
            @RequestHeader(value = "User-Agent",  required = false) String userAgent,
            @RequestHeader(value = "X-Longitude", required = false) String longitude,
            @RequestHeader(value = "X-Latitude",  required = false) String latitude,
            HttpServletRequest httpRequest) {

        if (request == null || request.getJourneyId() == null) {
            return ResponseEntity.badRequest().body("Missing journeyId");
        }

        String clientIp = getClientIp(httpRequest);
        try {
            rateLimiterService.checkLimit(
                    "QR_GENERATE:" + clientIp,
                    RateLimitConfig.QR_LIMIT,
                    RateLimitConfig.QR_WINDOW
            );
        } catch (RuntimeException ex) {
            return ResponseEntity.status(429)
                    .body("Too many QR generation requests. Please try again later.");
        }

        String ua    = (userAgent != null && !userAgent.isBlank()) ? userAgent : "Unknown";
        String jName = (request.getJourneyName() != null
                && !request.getJourneyName().isBlank())
                ? request.getJourneyName() : "Gold Loan";

        double lon = 0.0, lat = 0.0;
        try {
            if (longitude != null && !longitude.isBlank()) lon = Double.parseDouble(longitude);
            if (latitude  != null && !latitude.isBlank())  lat = Double.parseDouble(latitude);
        } catch (NumberFormatException ignored) {}

        String        journeyId = request.getJourneyId();
        String        qrCodeId  = UUID.randomUUID().toString();
        LocalDateTime now       = LocalDateTime.now();
        String        clientIpForSession = clientIp;

        String deeplink = "hdfcbank://qr/scan?qrId=" + qrCodeId
                + "&journeyId=" + journeyId
                + "&journeyName=" + jName;

        QrSession session = qrSessionRepository
            .findByJourneyId(journeyId)
            .orElse(new QrSession());
        session.setJourneyId(journeyId);
        session.setJourneyName(jName);
        session.setMobileNo("Pending");
        session.setScannedByMobile("Pending");
        session.setAuthType("QR_Auth");
        session.setAuthStatus("QR_INIT");
        session.setQrCodeId(qrCodeId);
        session.setDeeplink(deeplink);
        session.setTokenType("Bearer");
        session.setUserAgent(ua);
        session.setBrowserUsed(BrowserUtil.detect(ua));
        session.setDeviceLatitude(lat);
        session.setDeviceLongitude(lon);
        session.setIpAddress(clientIpForSession);
        if (session.getCreatedAt() == null) {
            session.setCreatedAt(now);
        }
        session.setQrExpiresAt(now.plusSeconds(30));
        session.setQrScannedAt(now.plusSeconds(30));
        session.setExpiresAt(now.plusSeconds(1799));
        qrSessionRepository.save(session);

        String qrBase64;
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix    = writer.encode(deeplink, BarcodeFormat.QR_CODE, 250, 250);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);
            qrBase64 = Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

        long nowEpoch = System.currentTimeMillis() / 1000;
        QrGenerateResponseData data = new QrGenerateResponseData(
                session.getQrSessionId(), qrCodeId, journeyId,
                nowEpoch, nowEpoch + 30,
                ua, jName, deeplink);

        return ResponseEntity.ok(new QrGenerateResponse(data, qrBase64));
    }


    @PostMapping("/api/v1/auth/qr/validate")
    public ResponseEntity<QrValidateResponse> validateQr(
            @RequestBody QrValidateRequest request,
            @RequestHeader(value = "x-mobile-no", required = false) String mobileNo,
            @RequestHeader(value = "X-devide-id", required = false) String devideId,
            @RequestHeader(value = "X-device-id", required = false) String deviceId,
            HttpServletRequest httpRequest) {

        if (request.getQrId() == null || request.getJourneyId() == null) {
            return ResponseEntity.status(400)
                    .body(new QrValidateResponse("FAILED", false,false, null, null,
                            "MISSING_REQUIRED_FIELD", "qrId and journeyId are required."));
        }

        QrSession session = qrSessionRepository
                .findByQrCodeId(request.getQrId()).orElse(null);
        if (session == null) {
            return ResponseEntity.status(404)
                    .body(new QrValidateResponse("FAILED", false,false, null, null,
                            "SESSION_NOT_FOUND", "QR session not found."));
        }

        if (request.getQrSessionId() != null
                && !session.getQrSessionId().equals(request.getQrSessionId())) {
            return ResponseEntity.status(401)
                    .body(new QrValidateResponse("FAILED", false, false, null, null,
                            "SESSION_MISMATCH", "Session ID mismatch."));
        }

        if (LocalDateTime.now().isAfter(session.getQrExpiresAt())) {
            session.setAuthStatus("QR_EXPIRED");
            qrSessionRepository.save(session);
            return ResponseEntity.status(401)
                    .body(new QrValidateResponse("FAILED", false,false, null, null    ,
                            "QR_EXPIRED", "QR code has expired. Please generate a new one."));
        }

        // Fill all remaining columns with real values
        String scannedBy = (mobileNo != null && !mobileNo.isBlank()) ? mobileNo : "Unknown";
        String resolvedDeviceId;
        if (devideId != null && !devideId.isBlank()) {
            resolvedDeviceId = devideId;
        } else if (deviceId != null && !deviceId.isBlank()) {
            resolvedDeviceId = deviceId;
        } else {
            resolvedDeviceId = "Unknown";
        }
        LocalDateTime scannedAt = LocalDateTime.now();

        session.setAuthStatus("QR_SCANNED");
        session.setMobileNo(scannedBy);          
        session.setScannedByMobile(scannedBy);    
        session.setDeviceId(resolvedDeviceId);
        session.setQrScannedAt(scannedAt);        
        qrSessionRepository.save(session);

        return ResponseEntity.ok(new QrValidateResponse("SUCCESS", true,true,
                request.getJourneyId(), session.getQrSessionId(),
            maskMobile(scannedBy), maskDeviceId(resolvedDeviceId),
                null, "QR scanned successfully."));
    }

    private String maskMobile(String mobileNo) {
        if (mobileNo == null || mobileNo.isBlank()) return "Unknown";
        if (mobileNo.equals("Unknown") || mobileNo.equals("Pending")) return mobileNo;
        if (mobileNo.length() <= 4) return "****";
        return "******" + mobileNo.substring(mobileNo.length() - 4);
    }

    private String maskDeviceId(String deviceId) {
        if (deviceId == null || deviceId.isBlank()) return "Unknown";
        if (deviceId.equals("Unknown") || deviceId.equals("Pending")) return deviceId;
        if (deviceId.length() <= 4) return "****";
        return "******" + deviceId.substring(deviceId.length() - 4);
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
