package in.bank.hdfc.auth.hybridAuth.dto;

import lombok.Data;

@Data
public class AuthDataDTO {
    private String accessToken;
    private String tokenType;
    private long expiresIn;

    public AuthDataDTO(String accessToken, String tokenType, long expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }
}
