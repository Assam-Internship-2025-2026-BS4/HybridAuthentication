package in.bank.hdfc.auth.hybridAuth.dto;

public class OtpInitResponse {
    private String status;
    private String journeyId;
    private String otpSessionId;
    private String otpExpiresIn;
    private String message;

    public OtpInitResponse(String status, String journeyId, String otpSessionId,
                            String otpExpiresIn, String message) {
        this.status       = status;
        this.journeyId    = journeyId;
        this.otpSessionId = otpSessionId;
        this.otpExpiresIn = otpExpiresIn;
        this.message      = message;
    }

    public String getStatus()       { return status; }
    public String getJourneyId()    { return journeyId; }
    public String getOtpSessionId() { return otpSessionId; }
    public String getOtpExpiresIn() { return otpExpiresIn; }
    public String getMessage()      { return message; }
}
