package in.bank.hdfc.auth.hybridAuth;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final WhatsappWebSocketHandler handler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(handler, "/ws")
                .setAllowedOrigins("*");
    }
}

