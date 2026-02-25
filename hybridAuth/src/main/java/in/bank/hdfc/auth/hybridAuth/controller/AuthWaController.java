package in.bank.hdfc.auth.hybridAuth.controller;

import in.bank.hdfc.auth.hybridAuth.entity.AuthSession;
import in.bank.hdfc.auth.hybridAuth.dto.WaInitRequest;
import in.bank.hdfc.auth.hybridAuth.dto.WaInitResponse;
import in.bank.hdfc.auth.hybridAuth.service.AuthSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/wa")
@RequiredArgsConstructor
public class AuthWaController {

    private final AuthSessionService authSessionService;

    @PostMapping("/init")
    public ResponseEntity<WaInitResponse> initWhatsAppAuth(
            @RequestBody @Valid WaInitRequest request
    ) {
        AuthSession session =
                authSessionService.initWhatsAppSession(request.getMobile());

        return ResponseEntity.ok(
                new WaInitResponse(session.getSessionToken())
        );
    }
}