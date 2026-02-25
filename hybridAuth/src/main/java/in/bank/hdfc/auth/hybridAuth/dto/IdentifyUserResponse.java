package in.bank.hdfc.auth.hybridAuth.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class IdentifyUserResponse {
    private String status; // VERIFIED / FAILED
    private String errorCode; // null if success
    private boolean whatsappRegistered;
}
