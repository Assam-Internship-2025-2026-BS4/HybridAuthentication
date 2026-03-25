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
    public String register(@RequestHeader("User-Agent") String userAgent,
            @RequestHeader("Authorization") String authorization,
            @RequestBody RegisterDeviceRequest request) {

        QRHeader header = new QRHeader(userAgent, authorization);

        return whatsappService.registerDevice(header, request);
    }

    @PostMapping("/login")

    public String login(@RequestHeader("User-Agent") String userAgent,
            @RequestHeader("Authorization") String authorization,
            @RequestBody WhatsappLoginRequest request) {

        QRHeader header = new QRHeader(userAgent, authorization);

        return whatsappService.createLoginSession(header, request);
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
        @RequestHeader("User-Agent") String userAgent,
        @RequestHeader("Authorization") String authorization,
        @PathVariable String sessionId) {

        QRHeader header = new QRHeader(userAgent, authorization);

        return whatsappService.getSessionStatus(header,sessionId);
    }
}