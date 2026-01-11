package webserver;

import java.util.Map;

public class TemplateEngine {
    public static String render(String html, Map<String, String> data) {
        StringBuilder sb = new StringBuilder(html);

        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = "{{" + entry.getKey() + "}}";
            String value = entry.getValue() != null ? entry.getValue() : "";

            int start = sb.indexOf(key);
            while(start != -1){
                sb.replace(start, start + key.length(), value);
                start = sb.indexOf(key, start + value.length());
            }
        }

        return sb.toString();
    }
}
