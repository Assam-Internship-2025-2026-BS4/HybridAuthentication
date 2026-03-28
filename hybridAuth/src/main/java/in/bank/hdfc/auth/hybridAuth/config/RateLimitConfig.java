package in.bank.hdfc.auth.hybridAuth.config;

public class RateLimitConfig {

    // OTP INIT → 5 / 10 min
    public static final int OTP_INIT_LIMIT = 5;
    public static final long OTP_INIT_WINDOW = 10 * 60 * 1000;

    // OTP VALIDATE → 10 / 5 min
    public static final int OTP_VALIDATE_LIMIT = 10;
    public static final long OTP_VALIDATE_WINDOW = 5 * 60 * 1000;

    // QR GENERATE → 10 / min
    public static final int QR_LIMIT = 10;
    public static final long QR_WINDOW = 60 * 1000;

    // WA INIT → 3 / 10 min
    public static final int WA_LIMIT = 3;
    public static final long WA_WINDOW = 10 * 60 * 1000;
}