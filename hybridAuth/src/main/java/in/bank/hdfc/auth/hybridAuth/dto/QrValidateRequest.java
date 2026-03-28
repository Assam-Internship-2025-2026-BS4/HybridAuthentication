package in.bank.hdfc.auth.hybridAuth.dto;

public class QrValidateRequest {
    private String qrId;
    private String journeyId;
    private String qrSessionId;

    public String getQrId()       { return qrId; }
    public void setQrId(String v) { this.qrId = v; }
    public String getJourneyId()  { return journeyId; }
    public void setJourneyId(String v) { this.journeyId = v; }
    public String getQrSessionId() { return qrSessionId; }
    public void setQrSessionId(String v) { this.qrSessionId = v; }
}
