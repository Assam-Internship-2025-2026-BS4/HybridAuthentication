package in.bank.hdfc.auth.hybridAuth.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PanHashUtil {

    public static String hash(String pan) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(pan.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes)
                sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("PAN hashing failed", e);
        }
    }
}