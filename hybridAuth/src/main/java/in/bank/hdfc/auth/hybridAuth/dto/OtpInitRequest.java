package in.bank.hdfc.auth.hybridAuth.dto;

public class OtpInitRequest {
    private String mobileNo;
    private String journeyId;
    private String journeyName;   // comes from /auth/init response, default "Gold Loan"

    public String getMobileNo()    { return mobileNo; }
    public void setMobileNo(String v) { this.mobileNo = v; }
    public String getJourneyId()   { return journeyId; }
    public void setJourneyId(String v) { this.journeyId = v; }
    public String getJourneyName() { return journeyName; }
    public void setJourneyName(String v) { this.journeyName = v; }
}
