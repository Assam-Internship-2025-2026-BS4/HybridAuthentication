package in.bank.hdfc.auth.hybridAuth.dto;

import lombok.Data;

@Data
public class StatusDTO {
    private String code;
    private String message;

    public StatusDTO(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
