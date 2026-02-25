package in.bank.hdfc.auth.hybridAuth.controller;

import in.bank.hdfc.auth.hybridAuth.entity.AuthSession;
import in.bank.hdfc.auth.hybridAuth.dto.ApproveSessionRequest;
import in.bank.hdfc.auth.hybridAuth.dto.SessionFetchResponse;
import in.bank.hdfc.auth.hybridAuth.service.AuthSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth/session")
@RequiredArgsConstructor
public class AuthSessionController {

    private final AuthSessionService authSessionService;

    // WhatsApp → Backend
    @PostMapping("/approve")
    public ResponseEntity<Void> approveSession(@RequestBody @Valid ApproveSessionRequest request) {
        authSessionService.approveSession(
                request.getSessionToken(),
                request.isApproved()
        );

        return ResponseEntity.ok().build();
    }

    // UI → Backend (polling)
    @GetMapping("/fetch")
    public ResponseEntity<SessionFetchResponse> fetchSession(
            @RequestParam String sessionToken
    ) {
        AuthSession session = authSessionService.fetchSession(sessionToken);
        return ResponseEntity.ok(
                new SessionFetchResponse(session.getStatus().name())
        );
    }


}
