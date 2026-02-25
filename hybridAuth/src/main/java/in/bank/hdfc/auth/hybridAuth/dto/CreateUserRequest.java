package in.bank.hdfc.auth.hybridAuth.dto;

import lombok.Data;

@Data
public class CreateUserRequest {

    private String name;
    private String mobileNumber;
    private String DOB;
    private String panNumber;
    private String email;
    private Boolean whatsAppRegistered;

}
