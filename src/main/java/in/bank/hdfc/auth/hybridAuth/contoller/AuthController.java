package in.bank.hdfc.auth.hybridAuth.contoller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.bank.hdfc.auth.hybridAuth.dto.ApiResponse;
import in.bank.hdfc.auth.hybridAuth.dto.AuthDataDTO;
import in.bank.hdfc.auth.hybridAuth.dto.AuthInitRequest;
import in.bank.hdfc.auth.hybridAuth.dto.AuthInitResponse;
import in.bank.hdfc.auth.hybridAuth.dto.JourneyHeader;
import in.bank.hdfc.auth.hybridAuth.dto.OtpValidateRequest;
import in.bank.hdfc.auth.hybridAuth.dto.OtpValidateResponse;
import in.bank.hdfc.auth.hybridAuth.dto.QRGenerateResponse;
import in.bank.hdfc.auth.hybridAuth.dto.QRHeader;
import in.bank.hdfc.auth.hybridAuth.dto.QrValidateResponse;
import in.bank.hdfc.auth.hybridAuth.dto.SessionDataDTO;
import in.bank.hdfc.auth.hybridAuth.dto.StatusDTO;
import in.bank.hdfc.auth.hybridAuth.dto.WaWebhookRequest;
import in.bank.hdfc.auth.hybridAuth.enums.AuthType;
import in.bank.hdfc.auth.hybridAuth.service.AuthService;
import in.bank.hdfc.auth.hybridAuth.service.NewService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final NewService newService;

    @PostMapping("/init")
    public ResponseEntity<ApiResponse<AuthDataDTO>> initAuth(@RequestHeader("X-journey-name") String journeyName) {
        JourneyHeader header = new JourneyHeader(journeyName);
        AuthDataDTO authData = newService.initiateAuth(header);
        StatusDTO status = new StatusDTO(
                "0000",
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

    // @GetMapping("/session/fetch/{sessionId}")
    // public SessionFetchResponse fetch(@PathVariable String sessionId) {
    // return authService.fetchSession(sessionId);
    // }

    @PostMapping("/wa/init")
    public AuthInitResponse waInit(
            @RequestBody AuthInitRequest request) {

        return authService.initiateAuth(
                request.getMobileNumber(),
                AuthType.WA);
    }

    @PostMapping("/otp/init")
    public AuthInitResponse otpInit(
            @RequestBody AuthInitRequest request) {

        return authService.initiateAuth(
                request.getMobileNumber(),
                AuthType.OTP);
    }

    @PostMapping("/otp/validate")
    public OtpValidateResponse validateOtp(
            @RequestBody OtpValidateRequest request) {

        return authService.validateOtp(
                request.getSessionId(),
                request.getOtp());
    }

    @PostMapping("/wa/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody WaWebhookRequest request) {

        authService.updateSessionStatus(
                request.getSessionId(),
                request.getAction());

        return ResponseEntity.ok("Updated");
    }

    // @PostMapping("/qr/generate")
    // public AuthInitResponse generateQr(@RequestBody AuthInitRequest request) {
    // return authService.initiateAuth(
    // request.getMobileNumber(),
    // AuthType.QR);
    // }

    // @PostMapping("/qr/validate")
    // public QrValidateResponse validateQr(@RequestParam String qrToken) {
    // return authService.validateQr(qrToken);
    // }

    // @GetMapping("")
    // public ResponseEntity<AuthSession> fetch(){
    // AuthSession authSession = new AuthSession();

    // return ResponseEntity.ok(authSession);
    // }

}
