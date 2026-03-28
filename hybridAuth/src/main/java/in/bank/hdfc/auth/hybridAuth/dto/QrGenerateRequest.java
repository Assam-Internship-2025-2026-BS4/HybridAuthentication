package in.bank.hdfc.auth.hybridAuth.dto;
public class QrGenerateRequest {
    private String journeyId;
    private String journeyName;
    public String getJourneyId() { return journeyId; }
    public void setJourneyId(String v) { this.journeyId = v; }
    public String getJourneyName() { return journeyName; }
    public void setJourneyName(String v) { this.journeyName = v; }
}
