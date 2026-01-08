package webserver.session;

public class CookieUtils {
    public static String getCookieValue(String cookieHeader, String key) {
        if (cookieHeader == null || cookieHeader.isBlank()) return null;

        // "sid=123; theme=dark"
        String[] parts = cookieHeader.split(";");
        for (String part : parts) {
            String trimmed = part.trim();
            int eq = trimmed.indexOf('=');
            if (eq < 0) continue;

            String k = trimmed.substring(0, eq).trim();
            String v = trimmed.substring(eq + 1).trim();

            if (k.equals(key)) return v;
        }
        return null;
    }

    public static String buildSetCookieSid(String sid) {
        return "sid=" + sid + "; Path=/";
    }

    public static String buildExpireSidCookie() {
        return "sid=; Max-Age=0; Path=/";
    }
}
