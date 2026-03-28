package in.bank.hdfc.auth.hybridAuth.dto;
public class QrInitResponse {
    private StatusBlock status;
    private QrInitResponseData data;
    public QrInitResponse(StatusBlock status, QrInitResponseData data) {
        this.status = status; this.data = data;
    }
    public StatusBlock getStatus() { return status; }
    public void setStatus(StatusBlock v) { this.status = v; }
    public QrInitResponseData getData() { return data; }
    public void setData(QrInitResponseData v) { this.data = v; }
}
