package in.bank.hdfc.auth.hybridAuth;

import java.net.URI;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WhatsappWebSocketHandler extends TextWebSocketHandler {

    private final WebSocketSessionStore sessionStore;

    public WhatsappWebSocketHandler(WebSocketSessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        URI uri = session.getUri();

        String query = uri.getQuery();

        String deviceToken = query.split("=")[1];

        sessionStore.addSession(deviceToken, session);

        System.out.println("Device connected: " + deviceToken);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {

        System.out.println("Connection closed");
    }
}