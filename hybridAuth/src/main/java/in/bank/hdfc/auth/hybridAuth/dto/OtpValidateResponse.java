package in.bank.hdfc.auth.hybridAuth.dto;

public class OtpValidateResponse {
    private String  status;
    private String  journeyId;
    private String  otpSessionId;
    private String  maskedMobileNo;
    private String  accessToken;
    private int     expiresIn;
    private Integer attemptsRemaining;
    private String  errorCode;
    private String  message;

    public OtpValidateResponse(String status, String journeyId, String otpSessionId,
                                String accessToken, int expiresIn,
                                Integer attemptsRemaining, String errorCode, String message) {
        this(status, journeyId, otpSessionId, null, accessToken, expiresIn,
                attemptsRemaining, errorCode, message);
    }

    public OtpValidateResponse(String status, String journeyId, String otpSessionId,
                                String maskedMobileNo, String accessToken, int expiresIn,
                                Integer attemptsRemaining, String errorCode, String message) {
        this.status            = status;
        this.journeyId         = journeyId;
        this.otpSessionId      = otpSessionId;
        this.maskedMobileNo    = maskedMobileNo;
        this.accessToken       = accessToken;
        this.expiresIn         = expiresIn;
        this.attemptsRemaining = attemptsRemaining;
        this.errorCode         = errorCode;
        this.message           = message;
    }

    public String  getStatus()            { return status; }
    public String  getJourneyId()         { return journeyId; }
    public String  getOtpSessionId()      { return otpSessionId; }
    public String  getMaskedMobileNo()    { return maskedMobileNo; }
    public String  getAccessToken()       { return accessToken; }
    public int     getExpiresIn()         { return expiresIn; }
    public Integer getAttemptsRemaining() { return attemptsRemaining; }
    public String  getErrorCode()         { return errorCode; }
    public String  getMessage()           { return message; }
}
