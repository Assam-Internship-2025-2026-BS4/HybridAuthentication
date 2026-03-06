package in.bank.hdfc.auth.hybridAuth.client;

public interface SmsClient {
    void sendOtp(String mobile, String otp);
}
