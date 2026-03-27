package in.bank.hdfc.auth.hybridAuth.contoller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import in.bank.hdfc.auth.hybridAuth.dto.*;
import in.bank.hdfc.auth.hybridAuth.service.WhatsappService;

@RestController
@RequestMapping("/api/v1/whatsapp")
@RequiredArgsConstructor
public class WhatsappController {

    private final WhatsappService whatsappService;

    @PostMapping("/register")
    public String register(
            @RequestBody RegisterDeviceRequest request) {

        // QRHeader header = new QRHeader(userAgent, authorization);

        return whatsappService.registerDevice(request);
    }

    @PostMapping("/login")

    public String login(
            @RequestBody WhatsappLoginRequest request) {

        // QRHeader header = new QRHeader(userAgent, authorization);

        return whatsappService.createLoginSession(request);
    }

    @PostMapping("/approve")

    public void approve(@RequestHeader("User-Agent") String userAgent,
            @RequestHeader("Authorization") String authorization,
            @RequestBody ApproveLoginRequest request) {

        QRHeader header = new QRHeader(userAgent, authorization);

        whatsappService.approveLogin(header, request);
    }

    @GetMapping("/status/{sessionId}")

    public String status(
        @PathVariable String sessionId) {

        // QRHeader header = new QRHeader(userAgent, authorization);

        return whatsappService.getSessionStatus(sessionId);
    }
}