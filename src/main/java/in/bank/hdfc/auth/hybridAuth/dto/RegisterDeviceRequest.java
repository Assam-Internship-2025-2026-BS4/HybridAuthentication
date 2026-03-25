package in.bank.hdfc.auth.hybridAuth.dto;

import lombok.Data;

@Data
public class RegisterDeviceRequest {

    private String mobileNumber;

    private String deviceId;

    private String deviceModel;
}