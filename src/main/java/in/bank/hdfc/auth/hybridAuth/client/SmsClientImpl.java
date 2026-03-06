package in.bank.hdfc.auth.hybridAuth.client;

import org.springframework.stereotype.Component;

@Component
public class SmsClientImpl implements SmsClient {

    @Override
    public void sendOtp(String mobile, String otp) {
        System.out.println("Sending OTP " + otp + " to mobile " + mobile);
    }
}