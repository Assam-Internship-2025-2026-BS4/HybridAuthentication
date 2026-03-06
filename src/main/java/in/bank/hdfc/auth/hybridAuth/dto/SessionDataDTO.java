package in.bank.hdfc.auth.hybridAuth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionDataDTO {
    private String customerType;
    private Boolean isKycComplient;
    private String sessionStatus;
}
