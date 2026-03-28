package in.bank.hdfc.auth.hybridAuth.util;

public final class BrowserUtil {
    private BrowserUtil() {}

    public static String detect(String userAgent) {
        if (userAgent == null || userAgent.isEmpty())                       return "Unknown";
        if (userAgent.contains("Edg"))                                      return "Edge";
        if (userAgent.contains("OPR") || userAgent.contains("Opera"))      return "Opera";
        if (userAgent.contains("Chrome") && !userAgent.contains("Edg"))    return "Chrome";
        if (userAgent.contains("Firefox"))                                  return "Firefox";
        if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) return "Safari";
        return "Unknown";
    }
}
