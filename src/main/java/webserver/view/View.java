package webserver.view;

import webserver.http.HTTPResponse;
import webserver.http.HTTPResponseWriter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface View {
    void render(String version,
                Map<String, Object> model,
                Map<String, String> headers,
                DataOutputStream dos,
                HTTPResponseWriter writer) throws Exception;
}

class TemplateView implements View {
    private static final String TEMPLATE_PATH = "./src/main/resources/template";
    private final String resolvedPath;

    public TemplateView(String resolvedPath) {
        this.resolvedPath = resolvedPath;
    }

    @Override
    public void render(String version,
                       Map<String, Object> model,
                       Map<String, String> headers,
                       DataOutputStream dos,
                       HTTPResponseWriter writer) throws Exception {
        String html = readFile(resolvedPath);

        String combinedHtml = combineFragments(html);

        String renderedHtml = TemplateEngine.render(combinedHtml, model);
        HTTPResponse httpResponse = HTTPResponse.ok("text/html", renderedHtml.getBytes());
        headers.forEach(httpResponse::addHeader);
        writer.write(dos, version, httpResponse);
    }

    private String combineFragments(String html) throws Exception {
        // HTML 내의 {{include:filename}} 패턴을 찾아서 치환 (fragment로 나눠놓은 부분)
        Pattern includePattern = Pattern.compile("\\{\\{include:(.+?)\\}\\}");
        Matcher matcher = includePattern.matcher(html);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String fileName = matcher.group(1).trim();
            String fragmentContent = readFile(TEMPLATE_PATH + "/fragment/" + fileName);
            // 조각 안에도 또 조각 재귀적으로 처리
            matcher.appendReplacement(sb, Matcher.quoteReplacement(combineFragments(fragmentContent)));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String readFile(String fileName) throws IOException {
        return Files.readString(Paths.get(TEMPLATE_PATH, fileName), StandardCharsets.UTF_8);
    }
}

class StaticView implements View {
    private final byte[] body;
    private final String contentType;

    public StaticView(byte[] body, String contentType) {
        this.body = body;
        this.contentType = contentType;
    }

    @Override
    public void render(String version,
                       Map<String, Object> model,
                       Map<String, String> headers,
                       DataOutputStream dos,
                       HTTPResponseWriter writer) throws Exception {
        HTTPResponse httpResponse = HTTPResponse.ok(contentType, body);
        headers.forEach(httpResponse::addHeader);
        writer.write(dos, version, httpResponse);
    }
}

class RedirectView implements View {
    private final String url;

    public RedirectView(String url) {
        this.url = url;
    }

    @Override
    public void render(String version,
                       Map<String, Object> model,
                       Map<String, String> headers,
                       DataOutputStream dos,
                       HTTPResponseWriter writer) throws Exception {
        HTTPResponse httpResponse = HTTPResponse.redirect(url);
        headers.forEach(httpResponse::addHeader);
        writer.write(dos, version, httpResponse);
    }
}