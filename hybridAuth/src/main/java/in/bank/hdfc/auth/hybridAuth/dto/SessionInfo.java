package in.bank.hdfc.auth.hybridAuth.dto;

import in.bank.hdfc.auth.hybridAuth.entity.OtpSession;
import in.bank.hdfc.auth.hybridAuth.entity.QrSession;
import in.bank.hdfc.auth.hybridAuth.entity.WaSession;
import java.time.LocalDateTime;

public class SessionInfo {
    private String        journeyId;
    private String        sessionId;   // otpSessionId / qrSessionId / waSessionId
    private String        authType;
    private String        authStatus;
    private String        mobileNo;
    private LocalDateTime expiresAt;
    private String        tokenType;

    public static SessionInfo from(OtpSession s) {
        SessionInfo i = new SessionInfo();
        i.journeyId  = s.getJourneyId();
        i.sessionId  = s.getOtpSessionId();
        i.authType   = s.getAuthType();
        i.authStatus = s.getAuthStatus();
        i.mobileNo   = s.getMobileNo();
        i.expiresAt  = s.getExpiresAt();
        i.tokenType  = s.getTokenType();
        return i;
    }

    public static SessionInfo from(QrSession s) {
        SessionInfo i = new SessionInfo();
        i.journeyId  = s.getJourneyId();
        i.sessionId  = s.getQrSessionId();
        i.authType   = s.getAuthType();
        i.authStatus = s.getAuthStatus();
        i.mobileNo   = s.getMobileNo();
        i.expiresAt  = s.getExpiresAt();
        i.tokenType  = s.getTokenType();
        return i;
    }

    public static SessionInfo from(WaSession s) {
        SessionInfo i = new SessionInfo();
        i.journeyId  = s.getJourneyId();
        i.sessionId  = s.getWaSessionId();
        i.authType   = s.getAuthType();
        i.authStatus = s.getAuthStatus();
        i.mobileNo   = s.getMobileNo();
        i.expiresAt  = s.getExpiresAt();
        i.tokenType  = s.getTokenType();
        return i;
    }

    public String        getJourneyId()  { return journeyId; }
    public String        getSessionId()  { return sessionId; }
    public String        getAuthType()   { return authType; }
    public String        getAuthStatus() { return authStatus; }
    public void          setAuthStatus(String v) { this.authStatus = v; }
    public String        getMobileNo()   { return mobileNo; }
    public LocalDateTime getExpiresAt()  { return expiresAt; }
    public String        getTokenType()  { return tokenType; }
}
