package in.bank.hdfc.auth.hybridAuth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdentifyUserRequest {
    @NotBlank
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Invalid mobile number")
    private String mobile;

    @NotBlank
    private String idType; // DOB or PAN

    @NotBlank
    private String idValue;
}
