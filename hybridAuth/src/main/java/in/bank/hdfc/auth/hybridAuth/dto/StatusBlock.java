package in.bank.hdfc.auth.hybridAuth.dto;
public class StatusBlock {
    private String code;
    private String message;
    public StatusBlock(String code, String message) { this.code = code; this.message = message; }
    public String getCode() { return code; }
    public void setCode(String v) { this.code = v; }
    public String getMessage() { return message; }
    public void setMessage(String v) { this.message = v; }
}
