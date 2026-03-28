package in.bank.hdfc.auth.hybridAuth.dto;

public class QrGenerateResponse {
    private QrGenerateResponseData data;
    private String qrBase64;

    public QrGenerateResponse(QrGenerateResponseData data, String qrBase64) {
        this.data     = data;
        this.qrBase64 = qrBase64;
    }

    public QrGenerateResponseData getData()    { return data; }
    public void setData(QrGenerateResponseData v) { this.data = v; }
    public String getQrBase64()                { return qrBase64; }
    public void setQrBase64(String v)          { this.qrBase64 = v; }
}
