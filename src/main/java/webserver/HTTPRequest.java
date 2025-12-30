package webserver;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class HTTPRequest {
    private String method;
    private String path;
    private String headers;
    private String version;

    public HTTPRequest(String method, String path, String headers, String version) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.version = version;
    }
    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHeaders() {
        return headers;
    }

    public String getVersion() {
        return version;
    }

    public String getContentType() {
        if (path.endsWith(".html") || path.endsWith("/")) return "text/html;charset=utf-8";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".ico")) return "image/x-icon";
        if (path.endsWith(".svg")) return "image/svg+xml";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        return "application/octet-stream"; // 알 수 없는 확장자로, 다운로드 처리
    }
}
