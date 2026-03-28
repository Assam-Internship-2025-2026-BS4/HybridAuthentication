package in.bank.hdfc.auth.hybridAuth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "otp_session")
public class OtpSession {

    @Id
    @Column(name = "otp_session_id", nullable = false, length = 50)
    private String otpSessionId;

    @Column(name = "journey_id", nullable = false, unique = true, length = 50)
    private String journeyId;

    @Column(name = "journey_name", nullable = false, length = 100)
    private String journeyName = "Gold Loan";

    @Column(name = "mobile_no", nullable = false, length = 10)
    private String mobileNo;

    @Column(name = "auth_type", nullable = false, length = 20)
    private String authType = "OTP";

    @Column(name = "auth_status", nullable = false, length = 20)
    private String authStatus = "Pending";

    @Column(name = "otp_code", nullable = false, length = 10)
    private String otpCode = "PENDING";

    @Column(name = "attempt", nullable = false)
    private int attempt = 0;

    @Column(name = "token_type", nullable = false, length = 20)
    private String tokenType = "Bearer";

    @Column(name = "otp_send_at")
private LocalDateTime otpSendAt;
public LocalDateTime getOtpSendAt() { return otpSendAt; }
public void setOtpSendAt(LocalDateTime v) { this.otpSendAt = v; }

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

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "otp_expires_at", nullable = false)
    private LocalDateTime otpExpiresAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (otpSessionId == null) otpSessionId = UUID.randomUUID().toString();
    }

    public String getOtpSessionId() { return otpSessionId; }
    public void setOtpSessionId(String v) { this.otpSessionId = v; }
    public String getJourneyId() { return journeyId; }
    public void setJourneyId(String v) { this.journeyId = v; }
    public String getJourneyName() { return journeyName; }
    public void setJourneyName(String v) { this.journeyName = v; }
    public String getMobileNo() { return mobileNo; }
    public void setMobileNo(String v) { this.mobileNo = v; }
    public String getAuthType() { return authType; }
    public void setAuthType(String v) { this.authType = v; }
    public String getAuthStatus() { return authStatus; }
    public void setAuthStatus(String v) { this.authStatus = v; }
    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String v) { this.otpCode = v; }
    public int getAttempt() { return attempt; }
    public void setAttempt(int v) { this.attempt = v; }
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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
    public LocalDateTime getOtpExpiresAt() { return otpExpiresAt; }
    public void setOtpExpiresAt(LocalDateTime v) { this.otpExpiresAt = v; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime v) { this.expiresAt = v; }
}
