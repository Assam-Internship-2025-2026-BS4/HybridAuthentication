package in.bank.hdfc.auth.hybridAuth.dto;
public class UserIdentifyResponse {
    private String status;
    private boolean identified;
    private boolean hasWhatsAppEnabled;
    public UserIdentifyResponse(String status, boolean identified, boolean hasWhatsAppEnabled) {
        this.status = status; this.identified = identified; this.hasWhatsAppEnabled = hasWhatsAppEnabled;
    }
    public String getStatus() { return status; }
    public boolean isIdentified() { return identified; }
    public boolean isHasWhatsAppEnabled() { return hasWhatsAppEnabled; }
}
