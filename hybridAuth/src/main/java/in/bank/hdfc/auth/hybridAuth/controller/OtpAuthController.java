package in.bank.hdfc.auth.hybridAuth.controller;

import in.bank.hdfc.auth.hybridAuth.service.OtpAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/otp")
@RequiredArgsConstructor
public class OtpAuthController {

    private final OtpAuthService otpService;

    @PostMapping("/init")
    public ResponseEntity<?> initOtp(@RequestParam String mobile) {
        return ResponseEntity.ok(
                otpService.initOtp(mobile).getSessionToken()
        );
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(
            @RequestParam String mobile,
            @RequestParam String otp
    ) {
        otpService.verifyOtp(mobile, otp);
        return ResponseEntity.ok().build();
    }
}