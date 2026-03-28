package in.bank.hdfc.auth.hybridAuth.dto;

public class QrGenerateResponseData {
    private String qrSessionId;
    private String qrCodeId;
    private String journeyId;
    private long   qrCreatedAt;
    private long   qrExpiresAt;
    private String userAgent;
    private String journeyName;
    private String deeplink;

    public QrGenerateResponseData(String qrSessionId, String qrCodeId, String journeyId,
                                   long qrCreatedAt, long qrExpiresAt,
                                   String userAgent, String journeyName, String deeplink) {
        this.qrSessionId = qrSessionId;
        this.qrCodeId    = qrCodeId;
        this.journeyId   = journeyId;
        this.qrCreatedAt = qrCreatedAt;
        this.qrExpiresAt = qrExpiresAt;
        this.userAgent   = userAgent;
        this.journeyName = journeyName;
        this.deeplink    = deeplink;
    }

    public String getQrSessionId() { return qrSessionId; }
    public String getQrCodeId()    { return qrCodeId; }
    public String getJourneyId()   { return journeyId; }
    public long   getQrCreatedAt() { return qrCreatedAt; }
    public long   getQrExpiresAt() { return qrExpiresAt; }
    public String getUserAgent()   { return userAgent; }
    public String getJourneyName() { return journeyName; }
    public String getDeeplink()    { return deeplink; }
}
