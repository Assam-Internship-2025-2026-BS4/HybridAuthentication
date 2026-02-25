package in.bank.hdfc.auth.hybridAuth.client;

import org.springframework.stereotype.Component;

@Component
public class WhatsAppClientImpl implements WhatsAppClient {

    @Override
    public void sendAuthPush(String mobile, String sessionId) {
        System.out.println("Sending WA push to " + mobile + 
                           " for session " + sessionId);
    }
}
