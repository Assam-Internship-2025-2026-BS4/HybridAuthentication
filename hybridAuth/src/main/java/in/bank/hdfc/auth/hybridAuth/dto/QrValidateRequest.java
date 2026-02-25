package in.bank.hdfc.auth.hybridAuth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QrValidateRequest {

    @NotBlank
    private String qrToken;

    @NotBlank
    private String mobile; // scanned user identity
}