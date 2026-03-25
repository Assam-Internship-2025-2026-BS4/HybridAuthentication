package in.bank.hdfc.auth.hybridAuth.dto;

import lombok.Data;

@Data
public class ApproveLoginRequest {

    private String sessionId;

    private String deviceToken;

    private Boolean approved;
}