package in.bank.hdfc.auth.hybridAuth.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import lombok.RequiredArgsConstructor;
import in.bank.hdfc.auth.hybridAuth.WebSocketSessionStore;
import in.bank.hdfc.auth.hybridAuth.dto.*;
import in.bank.hdfc.auth.hybridAuth.entity.*;
import in.bank.hdfc.auth.hybridAuth.repository.*;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class WhatsappService {

    private final UserDetailsRepository userRepository;

    private final WhatsappDeviceRepository deviceRepository;

    private final WhatsappLoginSessionRepository sessionRepository;

    private final WebSocketSessionStore sessionStore;

    public String registerDevice(RegisterDeviceRequest request) {

        User user = userRepository
                .findByMobileNumber(request.getMobileNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String deviceToken = UUID.randomUUID().toString();

        WhatsappDevice device = WhatsappDevice.builder()
                .customerId(user.getCustomerId())
                .deviceId(request.getDeviceId())
                .deviceToken(deviceToken)
                .deviceModel(request.getDeviceModel())
                .build();

        deviceRepository.save(device);

        user.setWhatsAppRegistered(true);

        userRepository.save(user);

        return deviceToken;
    }

    public String createLoginSession(WhatsappLoginRequest request) {

        User user = userRepository
                .findByMobileNumber(request.getMobileNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));

        WhatsappDevice device = deviceRepository
                .findByCustomerId(user.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Device not found"));

        String sessionId = UUID.randomUUID().toString();

        WhatsappLoginSession session = WhatsappLoginSession.builder()
                .sessionId(sessionId)
                .customerId(user.getCustomerId())
                .deviceToken(device.getDeviceToken())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusSeconds(60))
                .build();

        sessionRepository.save(session);

        WebSocketSession socket = sessionStore.getSession(device.getDeviceToken());

        if (socket != null && socket.isOpen()) {

            String message = """
                    {
                        "type":"LOGIN_REQUEST",
                        "sessionId":"%s",
                        "device":"%s",
                        "location":"Guwahati"
                    }
                    """.formatted(sessionId, request.getDeviceInfo());

            try {
                socket.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sessionId;
    }

    public void approveLogin(QRHeader header, ApproveLoginRequest request) {

        WhatsappLoginSession session = sessionRepository
                .findById(request.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.getDeviceToken().equals(request.getDeviceToken()))
            throw new RuntimeException("Invalid device");

        if (request.getApproved())
            session.setStatus("APPROVED");
        else
            session.setStatus("REJECTED");

        sessionRepository.save(session);
    }

    public String getSessionStatus(String sessionId) {
        WhatsappLoginSession session = sessionRepository
                .findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        return session.getStatus();
    }
}