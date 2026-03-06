package in.bank.hdfc.auth.hybridAuth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthInitResponse {
    
        private String sessionId;
        private String status;
}