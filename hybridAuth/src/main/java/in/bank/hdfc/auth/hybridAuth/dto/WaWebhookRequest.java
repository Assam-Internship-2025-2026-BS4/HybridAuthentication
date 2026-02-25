package in.bank.hdfc.auth.hybridAuth.dto;

import lombok.Data;

@Data
public class WaWebhookRequest {
    private String sessionId;
    private String action;
}
