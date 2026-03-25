package in.bank.hdfc.auth.hybridAuth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class WebSocketSessionStore {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void addSession(String deviceToken, WebSocketSession session) {

        sessions.put(deviceToken, session);
    }

    public WebSocketSession getSession(String deviceToken) {

        return sessions.get(deviceToken);
    }

    public void removeSession(String deviceToken) {

        sessions.remove(deviceToken);
    }
}