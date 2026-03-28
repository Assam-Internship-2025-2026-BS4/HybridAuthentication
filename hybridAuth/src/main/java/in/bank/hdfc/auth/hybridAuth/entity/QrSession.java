package in.bank.hdfc.auth.hybridAuth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "qr_session")
public class QrSession {

    @Id
    @Column(name = "qr_session_id", nullable = false, length = 50)
    private String qrSessionId;

    @Column(name = "journey_id", nullable = false, unique = true, length = 50)
    private String journeyId;

    @Column(name = "journey_name", nullable = false, length = 100)
    private String journeyName = "Gold Loan";

    @Column(name = "mobile_no", nullable = false, length = 10)
    private String mobileNo = "Unknown";

    @Column(name = "scanned_by_mobile", nullable = false, length = 10)
    private String scannedByMobile = "Unknown";

    @Column(name = "auth_type", nullable = false, length = 20)
    private String authType = "QR_Auth";

    @Column(name = "auth_status", nullable = false, length = 20)
    private String authStatus = "QR_INIT";

    @Column(name = "qr_code_id", nullable = false, length = 50)
    private String qrCodeId;

    @Column(name = "deeplink", nullable = false, columnDefinition = "TEXT")
    private String deeplink = "";

    @Column(name = "token_type", nullable = false, length = 20)
    private String tokenType = "Bearer";

    @Column(name = "user_agent", nullable = false, length = 500)
    private String userAgent = "Unknown";

    @Column(name = "browser_used", nullable = false, length = 100)
    private String browserUsed = "Unknown";

    @Column(name = "device_latitude", nullable = false)
    private Double deviceLatitude = 0.0;

    @Column(name = "device_longitude", nullable = false)
    private Double deviceLongitude = 0.0;

    @Column(name = "ip_address", nullable = false, length = 45)
    private String ipAddress = "0.0.0.0";

    @Column(name = "device_id", nullable = false, length = 100)
    private String deviceId = "Unknown";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "qr_expires_at", nullable = false)
    private LocalDateTime qrExpiresAt;

    @Column(name = "qr_scanned_at", nullable = false)
    private LocalDateTime qrScannedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null)    createdAt    = LocalDateTime.now();
        if (qrScannedAt == null)  qrScannedAt  = LocalDateTime.now();
        if (qrSessionId == null)  qrSessionId  = UUID.randomUUID().toString();
    }

    public String getQrSessionId() { return qrSessionId; }
    public void setQrSessionId(String v) { this.qrSessionId = v; }
    public String getJourneyId() { return journeyId; }
    public void setJourneyId(String v) { this.journeyId = v; }
    public String getJourneyName() { return journeyName; }
    public void setJourneyName(String v) { this.journeyName = v; }
    public String getMobileNo() { return mobileNo; }
    public void setMobileNo(String v) { this.mobileNo = v; }
    public String getScannedByMobile() { return scannedByMobile; }
    public void setScannedByMobile(String v) { this.scannedByMobile = v; }
    public String getAuthType() { return authType; }
    public void setAuthType(String v) { this.authType = v; }
    public String getAuthStatus() { return authStatus; }
    public void setAuthStatus(String v) { this.authStatus = v; }
    public String getQrCodeId() { return qrCodeId; }
    public void setQrCodeId(String v) { this.qrCodeId = v; }
    public String getDeeplink() { return deeplink; }
    public void setDeeplink(String v) { this.deeplink = v; }
    public String getTokenType() { return tokenType; }
    public void setTokenType(String v) { this.tokenType = v; }
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String v) { this.userAgent = v; }
    public String getBrowserUsed() { return browserUsed; }
    public void setBrowserUsed(String v) { this.browserUsed = v; }
    public Double getDeviceLatitude() { return deviceLatitude; }
    public void setDeviceLatitude(Double v) { this.deviceLatitude = v != null ? v : 0.0; }
    public Double getDeviceLongitude() { return deviceLongitude; }
    public void setDeviceLongitude(Double v) { this.deviceLongitude = v != null ? v : 0.0; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String v) { this.ipAddress = v; }
    public String getDeviceId() { return (deviceId != null && !deviceId.isBlank()) ? deviceId : "Unknown"; }
    public void setDeviceId(String v) {
        this.deviceId = (v != null && !v.isBlank()) ? v : "Unknown";
    }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
    public LocalDateTime getQrExpiresAt() { return qrExpiresAt; }
    public void setQrExpiresAt(LocalDateTime v) { this.qrExpiresAt = v; }
    public LocalDateTime getQrScannedAt() { return qrScannedAt; }
    public void setQrScannedAt(LocalDateTime v) { this.qrScannedAt = v; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime v) { this.expiresAt = v; }
}
