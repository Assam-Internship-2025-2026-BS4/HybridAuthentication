package in.bank.hdfc.auth.hybridAuth.dto;

public class SessionFetchRequest {
    private String journeyId;
    private String sessionId;   // qrSessionId or waSessionId
    private String qrId;        // optional — for qr/validate lookup by qrCodeId

    public String getJourneyId()  { return journeyId; }
    public void setJourneyId(String v) { this.journeyId = v; }
    public String getSessionId()  { return sessionId; }
    public void setSessionId(String v) { this.sessionId = v; }
    public String getQrId()       { return qrId; }
    public void setQrId(String v) { this.qrId = v; }
}
