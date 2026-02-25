package in.bank.hdfc.auth.hybridAuth.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Wa_InitRequest {
    @NotBlank
    private String mobile;
}
