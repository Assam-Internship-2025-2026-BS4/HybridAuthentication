package in.bank.hdfc.auth.hybridAuth.util;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;


@Component
public class JwtUtil {

    private final SecretKey secretKey;

    private static final long EXPIRATION_TIME = 1799 * 1000;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken() {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .subject("system-user")
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public long getExpirationInSeconds() {
        return 1799;
    }

    public String generateBase64Qr(String text) {
        try {
            int width = 250;
            int height = 250;

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF);
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);

            return Base64.getEncoder().encodeToString(baos.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Error generating QR", e);
        }
    }
}