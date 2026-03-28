package in.bank.hdfc.auth.hybridAuth.controller;

import in.bank.hdfc.auth.hybridAuth.dto.QrInitResponse;
import in.bank.hdfc.auth.hybridAuth.dto.QrInitResponseData;
import in.bank.hdfc.auth.hybridAuth.dto.StatusBlock;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthInitController {

    @PostMapping("/init")
    public ResponseEntity<QrInitResponse> initAuth(
            @RequestHeader(value = "journeyName", defaultValue = "Gold Loan") String journeyName) {

        String journeyId = UUID.randomUUID().toString();

        StatusBlock status = new StatusBlock("0000", "Request succeeded");
        QrInitResponseData data = new QrInitResponseData(journeyId, 1799, journeyName);
        return ResponseEntity.ok(new QrInitResponse(status, data));
    }
}
