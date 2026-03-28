package in.bank.hdfc.auth.hybridAuth.dto;

public class QrValidateResponse {
    private String  status;
    private boolean scanned;
    private boolean sessionValid;
    private String  journeyId;
    private String  qrSessionId;
    private String  maskedMobileNo;
    private String  deviceId;
    private String  errorCode;
    private String  message;

    public QrValidateResponse(String status, boolean scanned, boolean sessionValid, String journeyId,
                               String qrSessionId, String errorCode, String message) {
        this(status, scanned, sessionValid, journeyId, qrSessionId, null, null, errorCode, message);
    }

    public QrValidateResponse(String status, boolean scanned, boolean sessionValid, String journeyId,
                               String qrSessionId, String maskedMobileNo, String deviceId,
                               String errorCode, String message) {
        this.status      = status;
        this.scanned     = scanned;
        this.sessionValid = sessionValid;
        this.journeyId   = journeyId;
        this.qrSessionId = qrSessionId;
        this.maskedMobileNo = maskedMobileNo;
        this.deviceId = deviceId;
        this.errorCode   = errorCode;
        this.message     = message;
    }

    public String  getStatus()      { return status; }
    public boolean isScanned()      { return scanned; }
    public boolean isSessionValid() { return sessionValid; }
    public String  getJourneyId()   { return journeyId; }
    public String  getQrSessionId() { return qrSessionId; }
    public String  getMaskedMobileNo() { return maskedMobileNo; }
    public String  getDeviceId() { return deviceId; }
    public String  getErrorCode()   { return errorCode; }
    public String  getMessage()     { return message; }
}
