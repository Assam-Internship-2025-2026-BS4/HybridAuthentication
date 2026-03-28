package in.bank.hdfc.auth.hybridAuth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionFetchResponse {
    private String status;
    private String journeyId;
    private String sessionId;
    private String authStatus;
    private int    expiresIn;
    private String accessToken;
    private String errorCode;
    private String message;

    public SessionFetchResponse(String status, String journeyId, String sessionId,
                                 String authStatus, int expiresIn, String accessToken,
                                 String errorCode, String message) {
        this.status      = status;
        this.journeyId   = journeyId;
        this.sessionId   = sessionId;
        this.authStatus  = authStatus;
        this.expiresIn   = expiresIn;
        this.accessToken = accessToken;
        this.errorCode   = errorCode;
        this.message     = message;
    }

    public String getStatus()      { return status; }
    public String getJourneyId()   { return journeyId; }
    public String getSessionId()   { return sessionId; }
    public String getAuthStatus()  { return authStatus; }
    public int    getExpiresIn()   { return expiresIn; }
    public String getAccessToken() { return accessToken; }
    public String getErrorCode()   { return errorCode; }
    public String getMessage()     { return message; }
}