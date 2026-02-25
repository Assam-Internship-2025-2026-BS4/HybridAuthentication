package in.bank.hdfc.auth.hybridAuth.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class DobCryptoUtil {

    private static final String KEY = "1234567812345678";

    public static String encrypt(String dob) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE,
                    new SecretKeySpec(KEY.getBytes(), "AES"));
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(dob.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("DOB encryption failed");
        }
    }

    public static String decrypt(String encryptedDob) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE,
                    new SecretKeySpec(KEY.getBytes(), "AES"));
            return new String(cipher.doFinal(
                    Base64.getDecoder().decode(encryptedDob)));
        } catch (Exception e) {
            throw new RuntimeException("DOB decryption failed");
        }
    }
}