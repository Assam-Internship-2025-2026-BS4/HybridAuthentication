package in.bank.hdfc.auth.hybridAuth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApproveSessionResponse {
    private String sessionToken;
    private String status;
    private String message;
}