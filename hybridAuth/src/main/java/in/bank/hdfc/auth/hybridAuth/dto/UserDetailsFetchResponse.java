package in.bank.hdfc.auth.hybridAuth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDetailsFetchResponse {

    private String mobile;
    private boolean whatsappRegistered;
    private boolean active;
}