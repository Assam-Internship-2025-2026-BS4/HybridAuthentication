package in.bank.hdfc.auth.hybridAuth.dto;
public class QrInitResponseData {
    private String journeyId;
    private long expiresIn;
    private String journeyName;
    public QrInitResponseData(String journeyId, long expiresIn, String journeyName) {
        this.journeyId = journeyId; this.expiresIn = expiresIn; this.journeyName = journeyName;
    }
    public String getJourneyId() { return journeyId; }
    public void setJourneyId(String v) { this.journeyId = v; }
    public long getExpiresIn() { return expiresIn; }
    public void setExpiresIn(long v) { this.expiresIn = v; }
    public String getJourneyName() { return journeyName; }
    public void setJourneyName(String v) { this.journeyName = v; }
}
