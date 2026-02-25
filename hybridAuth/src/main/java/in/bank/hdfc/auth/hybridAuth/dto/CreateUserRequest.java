package in.bank.hdfc.auth.hybridAuth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    private String mobile;
    private String pan;
    private String dob; // yyyy-MM-dd
    private boolean whatsappRegistered;
    private boolean active;
}