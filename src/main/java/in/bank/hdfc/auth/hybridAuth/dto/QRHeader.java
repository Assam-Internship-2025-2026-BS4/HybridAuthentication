package in.bank.hdfc.auth.hybridAuth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QRHeader {
    private String userAgent;
    private String authorization;
}
