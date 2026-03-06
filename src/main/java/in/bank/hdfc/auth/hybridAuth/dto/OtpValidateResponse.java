package in.bank.hdfc.auth.hybridAuth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OtpValidateResponse {
    String sessionId;
    String status;
    boolean valid;
}
