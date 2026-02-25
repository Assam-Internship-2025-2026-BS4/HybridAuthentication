package in.bank.hdfc.auth.hybridAuth.client;


public interface WhatsAppClient {
    void sendAuthPush(String mobile, String sessionId);
}
