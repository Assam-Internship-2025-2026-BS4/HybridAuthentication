package in.bank.hdfc.auth.hybridAuth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserResponse {

    private String customerId;

    private String accountNumber;

    private String name;

    private String mobileNumber;

    private String DOB;

    private String panNumber;

    private String email;

    private Boolean whatsAppRegistered;
}
