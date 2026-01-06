package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class HTTPRequestParser {
    public HTTPRequest parse(BufferedReader br) throws IOException {
        // 1. requestLine 읽기
        String requestLine = br.readLine();
        if (requestLine == null) return null;

        String[] tokens = parseRequestLine(requestLine);
        HTTPMethod method = HTTPMethod.valueOf(tokens[0]);
        String requestTarget = tokens[1];
        String version = tokens[2];

        // 2. 경로 및 파라미터 분리
        String path = extractPath(requestTarget);
        HashMap<String, String> queryParams = extractQueryParams(requestTarget);

        // 3. 헤더 파싱
        StringBuilder rawHeaders = new StringBuilder().append(requestLine).append("\r\n");
        HashMap<String, String> headers = parseHeaders(br, rawHeaders);

        return new HTTPRequest(method, path, queryParams, headers, rawHeaders.toString(), version);
    }

    private String[] parseRequestLine(String requestLine) {
        String[] tokens = requestLine.split(" ");
        if (tokens.length != 3) {
            throw new IllegalArgumentException("Invalid HTTP Request Line");
        }
        return tokens;
    }

    private String extractPath(String requestTarget) {
        int qMarkIndex = requestTarget.indexOf('?');
        return (qMarkIndex >= 0) ? requestTarget.substring(0, qMarkIndex) : requestTarget;
    }

    private HashMap<String, String> extractQueryParams(String requestTarget) {
        int qMarkIndex = requestTarget.indexOf('?');
        if (qMarkIndex < 0) return new HashMap<>();

        return parseQueryString(requestTarget.substring(qMarkIndex + 1));
    }

    private HashMap<String, String> parseHeaders(BufferedReader br, StringBuilder rawHeaders) throws IOException {
        HashMap<String, String> headers = new HashMap<>();
        String line;

        // HTTP/1.1 HTTP 요청을 한 번 보내고 connection이 종료되지 않고 유지된다.
        // 한 번의 HTTP 요청 헤더는 빈 줄로 종료되며, 연결이 유지되는 경우 readLine()은 null을 반환하지 않기 때문에
        // 빈 줄을 기준으로 헤더 읽기를 종료해야 한다.
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            rawHeaders.append(line).append("\r\n");
            int colonIndex = line.indexOf(":");
            if (colonIndex > 0) {
                String key = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();
                headers.put(key, value);
            }
        }
        return headers;
    }

    private HashMap<String, String> parseQueryString(String queryString) {
        HashMap<String, String> query = new HashMap<>();
        if (queryString == null || queryString.isEmpty()) return query;

        for (String pair : queryString.split("&")) {
            String[] tokens = pair.split("=", 2);
            String key = urlDecode(tokens[0]);
            String value = (tokens.length == 2) ? urlDecode(tokens[1]) : "";
            query.put(key, value);
        }
        return query;
    }

    private String urlDecode(String s) {
        return java.net.URLDecoder.decode(s, java.nio.charset.StandardCharsets.UTF_8);
    }

}
