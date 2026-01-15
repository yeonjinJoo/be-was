package webserver.view;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateEngine {
    private static final Pattern SECTION_PATTERN = Pattern.compile("\\{\\{#(.+?)\\}\\}(.*?)\\{\\{/\\1\\}\\}", Pattern.DOTALL);
    private static final Pattern TAG_PATTERN = Pattern.compile("\\{\\{(.+?)\\}\\}");

    public static String render(String html, Map<String, Object> model) {
        String processedHtml = processSections(html, model);

        Matcher matcher = TAG_PATTERN.matcher(processedHtml);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String key = matcher.group(1).trim();
            // model에서 값을 찾고, 없으면 빈 문자열로 대체
            Object value = model.getOrDefault(key, "");
            String str = (value == null) ? "" : value.toString();
            matcher.appendReplacement(sb, Matcher.quoteReplacement(str));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String processSections(String html, Map<String, Object> model) {
        Matcher matcher = SECTION_PATTERN.matcher(html);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String key = matcher.group(1).trim();
            String content = matcher.group(2); // 섹션 내부의 HTML/JS

            // 모델에서 해당 키가 true인 경우에만 내용을 남기고, 아니면 삭제
            boolean shouldRender = false;
            Object condition = model.get(key);
            if (condition instanceof Boolean) {
                shouldRender = (Boolean) condition;
            }

            matcher.appendReplacement(sb, Matcher.quoteReplacement(shouldRender ? content : ""));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}