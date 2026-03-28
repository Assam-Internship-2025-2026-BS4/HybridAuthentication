package in.bank.hdfc.auth.hybridAuth.dto;
public class UserIdentifyRequest {
    private String mobileNo;
    private String dob;
    private String pan;
    public UserIdentifyRequest() {}
    public String getMobileNo() { return mobileNo; }
    public void setMobileNo(String v) { this.mobileNo = v; }
    public String getDob() { return dob; }
    public void setDob(String v) { this.dob = v; }
    public String getPan() { return pan; }
    public void setPan(String v) { this.pan = v; }
    public boolean isValid() { return mobileNo != null && (dob != null || pan != null); }
}
