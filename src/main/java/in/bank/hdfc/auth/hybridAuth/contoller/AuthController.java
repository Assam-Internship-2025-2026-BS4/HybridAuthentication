package in.bank.hdfc.auth.hybridAuth.contoller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.bank.hdfc.auth.hybridAuth.dto.ApiResponse;
import in.bank.hdfc.auth.hybridAuth.dto.AuthDataDTO;
import in.bank.hdfc.auth.hybridAuth.dto.JourneyHeader;
import in.bank.hdfc.auth.hybridAuth.dto.QRGenerateResponse;
import in.bank.hdfc.auth.hybridAuth.dto.QRHeader;
import in.bank.hdfc.auth.hybridAuth.dto.QrValidateResponse;
import in.bank.hdfc.auth.hybridAuth.dto.SessionDataDTO;
import in.bank.hdfc.auth.hybridAuth.dto.StatusDTO;
import in.bank.hdfc.auth.hybridAuth.service.AuthService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService newService;

    @PostMapping("/init")
    public ResponseEntity<ApiResponse<AuthDataDTO>> initAuth(@RequestHeader("X-journey-name") String journeyName) {
        JourneyHeader header = new JourneyHeader(journeyName);
        AuthDataDTO authData = newService.initiateAuth(header);
        StatusDTO status = new StatusDTO(
                "200",
                "Request succeeded");
        ApiResponse<AuthDataDTO> response = new ApiResponse<>(status, authData);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/qr/generate")
    public QRGenerateResponse generateQr(@RequestHeader("User-Agent") String userAgent,
            @RequestHeader("Authorization") String authorization) {
        QRHeader header = new QRHeader(userAgent, authorization);
        return newService.qrGenerate(header);
    }

    @GetMapping("/session/fetch/{qrId}")
    public ApiResponse<SessionDataDTO> fetchSession(
            @PathVariable String qrId,
            @RequestHeader("User-Agent") String userAgent,
            @RequestHeader("Authorization") String token) {

        QRHeader header = new QRHeader(userAgent, token);

        SessionDataDTO data = newService.fetchSession(header, qrId);

        StatusDTO status = new StatusDTO("0000", "Request succeeded");

        return new ApiResponse<>(status, data);
    }

    @PostMapping("/qr/validate/{qrId}")
    public ResponseEntity<QrValidateResponse> validateQR(
            @RequestHeader("User-Agent") String userAgent,
            @RequestHeader("Authorization") String authorization,
            @PathVariable String qrId) {

        QRHeader header = new QRHeader(userAgent, authorization);

        QrValidateResponse response = newService.validateQR(header, qrId);

        return ResponseEntity.ok(response);
    }
}
