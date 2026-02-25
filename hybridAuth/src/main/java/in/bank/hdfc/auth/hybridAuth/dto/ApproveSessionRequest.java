package in.bank.hdfc.auth.hybridAuth.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApproveSessionRequest {

    @NotBlank
    private String sessionToken;

    private boolean approved;

}
