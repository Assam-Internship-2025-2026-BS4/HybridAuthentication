package in.bank.hdfc.auth.hybridAuth.dto;

import lombok.Data;

@Data
public class WhatsappLoginRequest {

    private String mobileNumber;

    private String deviceInfo;
}