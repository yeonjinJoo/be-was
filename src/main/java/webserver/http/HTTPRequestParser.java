package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.session.CookieUtils;
import webserver.WebServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class HTTPRequestParser {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    public HTTPRequest parse(InputStream in) throws IOException {
        // 1. requestLine 읽기
        String requestLine = readLine(in);
        if (requestLine == null) return null;
        if (requestLine.isEmpty()) return null;

        String[] tokens = parseRequestLine(requestLine);
        HTTPMethod method = HTTPMethod.valueOf(tokens[0]);
        String requestTarget = tokens[1];
        String version = tokens[2];

        // 2. 경로 및 파라미터 분리
        String path = extractPath(requestTarget);
        HashMap<String, String> queryParams = extractQueryParams(requestTarget);

        // 3. 헤더 파싱
        StringBuilder rawHeaders = new StringBuilder().append(requestLine).append("\r\n");
        HashMap<String, String> headers = parseHeaders(in, rawHeaders);

        // 4. sid 파싱
        String cookieValue = headers.get("cookie");
        String sid = cookieValue == null ? null : CookieUtils.getCookieValue(cookieValue, "sid");

        // 5. 바디 파싱
        HashMap<String, String> bodyParams = new HashMap<>();
        byte[] rawBody = new byte[0];

        String cl = headers.get("content-length");
        if (cl != null) {
            int contentLength = Integer.parseInt(cl);
            if (contentLength > 0) {
                rawBody = readBytes(in, contentLength);

                String contentType = headers.getOrDefault("content-type", "");
                if (contentType.startsWith("application/x-www-form-urlencoded")) {
                    String bodyStr = new String(rawBody, StandardCharsets.UTF_8);
                    bodyParams = parseQueryString(bodyStr);
                } else if (contentType.startsWith("multipart/form-data")) {
                    // multipart 텍스트/파일 파싱은 분리된 MultipartParser에서.
                } else {
                    throw new IllegalArgumentException("처리할 수 없는 body입니다.");
                }
            }
        }

        return new HTTPRequest(
                method,
                path,
                queryParams,
                headers,
                bodyParams,
                rawBody,
                new HashMap<>(),
                rawHeaders.toString(),
                version,
                sid
        );
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

    private HashMap<String, String> parseHeaders(InputStream in, StringBuilder rawHeaders) throws IOException {
        HashMap<String, String> headers = new HashMap<>();
        String line;

        // HTTP/1.1 HTTP 요청을 한 번 보내고 connection이 종료되지 않고 유지된다.
        // 한 번의 HTTP 요청 헤더는 빈 줄로 종료되며, 연결이 유지되는 경우 readLine()은 null을 반환하지 않기 때문에
        // 빈 줄을 기준으로 헤더 읽기를 종료해야 한다.
        while ((line = readLine(in)) != null && !line.isEmpty()) {
            rawHeaders.append(line).append("\r\n");
            int colonIndex = line.indexOf(":");
            if (colonIndex > 0) {
                String key = line.substring(0, colonIndex).trim().toLowerCase();
                String value = line.substring(colonIndex + 1).trim();
                headers.put(key, value);
            }
        }
        return headers;
    }

    private String readLine(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        int prev = -1;
        int cur;

        while ((cur = in.read()) != -1) {
            if (prev == '\r' && cur == '\n') {
                sb.setLength(sb.length() - 1); // remove '\r'
                break;
            }
            sb.append((char) cur);
            prev = cur;
        }

        if (cur == -1 && sb.length() == 0) return null;
        return sb.toString();
    }

    private byte[] readBytes(InputStream in, int len) throws IOException {
        byte[] buf = new byte[len];
        int off = 0;
        while (off < len) {
            int r = in.read(buf, off, len - off);
            if (r == -1) throw new IOException("Unexpected EOF while reading body");
            off += r;
        }
        return buf;
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
