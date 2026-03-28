package in.bank.hdfc.auth.hybridAuth.dto;

public class UserDetailsFetchResponse {
    private String status;
    private String customerId;
    private String customerName;
    private String mobileNo;
    private String lastLoginTime;
    private String journeyId;
    private String sessionId;
    private String errorCode;

    public UserDetailsFetchResponse(String status, String customerId, String customerName,
                                     String mobileNo, String lastLoginTime,
                                     String journeyId, String sessionId, String errorCode) {
        this.status        = status;
        this.customerId    = customerId;
        this.customerName  = customerName;
        this.mobileNo      = mobileNo;
        this.lastLoginTime = lastLoginTime;
        this.journeyId     = journeyId;
        this.sessionId     = sessionId;
        this.errorCode     = errorCode;
    }

    public String getStatus()        { return status; }
    public String getCustomerId()    { return customerId; }
    public String getCustomerName()  { return customerName; }
    public String getMobileNo()      { return mobileNo; }
    public String getLastLoginTime() { return lastLoginTime; }
    public String getJourneyId()     { return journeyId; }
    public String getSessionId()     { return sessionId; }
    public String getErrorCode()     { return errorCode; }
}
