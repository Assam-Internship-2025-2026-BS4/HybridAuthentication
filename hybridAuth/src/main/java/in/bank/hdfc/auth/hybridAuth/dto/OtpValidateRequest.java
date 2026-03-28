package in.bank.hdfc.auth.hybridAuth.dto;

public class OtpValidateRequest {
    private String mobileNo;
    private String journeyId;
    private String otpSessionId;
    private String otp;

    public String getMobileNo()    { return mobileNo; }
    public void setMobileNo(String v) { this.mobileNo = v; }
    public String getJourneyId()   { return journeyId; }
    public void setJourneyId(String v) { this.journeyId = v; }
    public String getOtpSessionId() { return otpSessionId; }
    public void setOtpSessionId(String v) { this.otpSessionId = v; }
    public String getOtp()         { return otp; }
    public void setOtp(String v)   { this.otp = v; }
}
