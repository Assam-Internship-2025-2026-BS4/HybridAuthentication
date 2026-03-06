package in.bank.hdfc.auth.hybridAuth.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QRGenerateResponse {
    private String qrId;

    private LocalDateTime createdAt;

    private LocalDateTime expiresIn;

    private String deeplink;
}
