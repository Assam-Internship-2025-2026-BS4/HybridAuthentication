package in.bank.hdfc.auth.hybridAuth.dto;

public class WaWebhookRequest {
    private String journeyId;
    private String waReply;  // "YES" or "NO"

    public WaWebhookRequest() {}

    public WaWebhookRequest(String journeyId, String waReply) {
        this.journeyId = journeyId;
        this.waReply = waReply;
    }

    public String getJourneyId() { return journeyId; }
    public void setJourneyId(String v) { this.journeyId = v; }
    
    public String getWaReply() { return waReply; }
    public void setWaReply(String v) { this.waReply = v; }
}
