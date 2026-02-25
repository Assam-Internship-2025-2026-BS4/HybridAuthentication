package in.bank.hdfc.auth.hybridAuth.dto.dev;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DevReq {
    private String sessionToken;
    private String qrToken;
}
