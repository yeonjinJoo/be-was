package webserver.view;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateEngine {
    private static final Pattern TAG_PATTERN = Pattern.compile("\\{\\{(.+?)\\}\\}");

    public static String render(String html, Map<String, Object> model) {
        Matcher matcher = TAG_PATTERN.matcher(html);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String key = matcher.group(1).trim();
            // model에서 값을 찾고, 없으면 빈 문자열로 대체
            Object value = model.getOrDefault(key, "");
            matcher.appendReplacement(sb, Matcher.quoteReplacement(value.toString()));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}