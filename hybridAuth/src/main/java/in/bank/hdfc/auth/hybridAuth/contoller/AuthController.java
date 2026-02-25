package in.bank.hdfc.auth.hybridAuth.contoller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.bank.hdfc.auth.hybridAuth.dto.AuthInitRequest;
import in.bank.hdfc.auth.hybridAuth.dto.AuthInitResponse;
import in.bank.hdfc.auth.hybridAuth.dto.OtpValidateRequest;
import in.bank.hdfc.auth.hybridAuth.dto.OtpValidateResponse;
import in.bank.hdfc.auth.hybridAuth.dto.QrValidateResponse;
import in.bank.hdfc.auth.hybridAuth.dto.SessionFetchResponse;
import in.bank.hdfc.auth.hybridAuth.dto.WaWebhookRequest;
import in.bank.hdfc.auth.hybridAuth.enums.AuthType;
import in.bank.hdfc.auth.hybridAuth.service.AuthService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

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

    

    @GetMapping("/session/fetch/{sessionId}")
    public SessionFetchResponse fetch(@PathVariable String sessionId) {
        return authService.fetchSession(sessionId);
    }

    @PostMapping("/qr/generate")
    public AuthInitResponse generateQr(@RequestBody AuthInitRequest request) {
        return authService.initiateAuth(
                request.getMobileNumber(),
                AuthType.QR);
    }

    @PostMapping("/qr/validate")
    public QrValidateResponse validateQr(@RequestParam String qrToken) {
        return authService.validateQr(qrToken);
    }

    // @GetMapping("")
    // public ResponseEntity<AuthSession> fetch(){
    // AuthSession authSession = new AuthSession();

    // return ResponseEntity.ok(authSession);
    // }
}
