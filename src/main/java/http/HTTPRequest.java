package http;

import java.util.HashMap;

public class HTTPRequest {
    private HTTPMethod method;
    private String path;
    private HashMap<String, String> queryParams;
    private HashMap<String, String> headers;
    private String rawHeaders;
    private String version;

    public HTTPRequest(HTTPMethod method,
                       String path,
                       HashMap<String, String> queryParams,
                       HashMap<String, String> headers,
                       String rawHeaders,
                       String version) {
        this.method = method;
        this.path = path;
        this.queryParams = queryParams;
        this.headers = headers;
        this.rawHeaders = rawHeaders;
        this.version = version;
    }
    public HTTPMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public HashMap<String, String> getQueryParams() { return queryParams; }

    public String getRawHeaders() {
        return rawHeaders;
    }

    public String getVersion() {
        return version;
    }
}
