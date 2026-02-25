package in.bank.hdfc.auth.hybridAuth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IdentifyUserResponse {
    boolean userExists;
    boolean whatsAppRegistered;
    String name;
}
