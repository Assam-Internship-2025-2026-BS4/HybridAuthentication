package in.bank.hdfc.auth.hybridAuth.util;

import java.security.MessageDigest;
import java.util.Random;

public class OtpUtil {

    public static String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    public static String hashOtp(String otp) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(otp.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("OTP hashing failed");
        }
    }
}