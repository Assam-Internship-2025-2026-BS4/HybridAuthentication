package in.bank.hdfc.auth.hybridAuth.dto;

public class WaInitRequest {
    private String journeyId;
    private String journeyName;   // comes from /auth/init response, default "Gold Loan"
    private String mobileNo;

    public WaInitRequest() {}

    public String getJourneyId()   { return journeyId; }
    public void setJourneyId(String v) { this.journeyId = v; }
    public String getJourneyName() { return journeyName; }
    public void setJourneyName(String v) { this.journeyName = v; }
    public String getMobileNo()    { return mobileNo; }
    public void setMobileNo(String v) { this.mobileNo = v; }
}
