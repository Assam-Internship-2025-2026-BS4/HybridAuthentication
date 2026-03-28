package in.bank.hdfc.auth.hybridAuth.dto;

public class WaInitResponse {
    private String journeyId;
    private String waSessionId;
    private String authStatus;
    private String maskedMobileNo;
    private String waSessionCreatedAt;
    private String waSessionExpiresAt;
    private String message;

    public WaInitResponse(String journeyId, String waSessionId, String authStatus,
                          String maskedMobileNo, String waSessionCreatedAt,
                          String waSessionExpiresAt, String message) {
        this.journeyId         = journeyId;
        this.waSessionId       = waSessionId;
        this.authStatus        = authStatus;
        this.maskedMobileNo    = maskedMobileNo;
        this.waSessionCreatedAt = waSessionCreatedAt;
        this.waSessionExpiresAt = waSessionExpiresAt;
        this.message           = message;
    }

    public String getJourneyId()          { return journeyId; }
    public String getWaSessionId()        { return waSessionId; }
    public String getAuthStatus()         { return authStatus; }
    public String getMaskedMobileNo()     { return maskedMobileNo; }
    public String getWaSessionCreatedAt() { return waSessionCreatedAt; }
    public String getWaSessionExpiresAt() { return waSessionExpiresAt; }
    public String getMessage()            { return message; }
}
