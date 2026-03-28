package in.bank.hdfc.auth.hybridAuth.dto;

public class WaWebhookResponse {
    private String status;
    private String journeyId;
    private String authStatus;
    private String waReply;
    private String message;

    public WaWebhookResponse(String status, String journeyId, String authStatus,
                             String waReply, String message) {
        this.status = status;
        this.journeyId = journeyId;
        this.authStatus = authStatus;
        this.waReply = waReply;
        this.message = message;
    }

    public String getStatus() { return status; }
    public String getJourneyId() { return journeyId; }
    public String getAuthStatus() { return authStatus; }
    public String getWaReply() { return waReply; }
    public String getMessage() { return message; }
}
