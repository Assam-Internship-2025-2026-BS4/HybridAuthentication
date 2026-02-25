package in.bank.hdfc.auth.hybridAuth.controller;

import in.bank.hdfc.auth.hybridAuth.dto.QrGenerateResponse;
import in.bank.hdfc.auth.hybridAuth.dto.QrValidateRequest;
import in.bank.hdfc.auth.hybridAuth.entity.AuthSession;
import in.bank.hdfc.auth.hybridAuth.service.AuthQrService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/qr")
@RequiredArgsConstructor
public class AuthQrController {

    private final AuthQrService qrService;

    @PostMapping("/generate")
    public ResponseEntity<QrGenerateResponse> generateQr() {

        AuthSession session = qrService.generateQrSession();

        return ResponseEntity.ok(
                new QrGenerateResponse(
                        session.getSessionToken(),
                        session.getQrToken()
                )
        );
    }

    @PostMapping("/validate")
    public ResponseEntity<Void> validateQr(
            @RequestBody @Valid QrValidateRequest request
    ) {
        qrService.validateQr(
                request.getQrToken(),
                request.getMobile()
        );
        return ResponseEntity.ok().build();
    }
}