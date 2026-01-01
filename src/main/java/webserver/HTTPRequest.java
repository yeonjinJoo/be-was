package webserver;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashMap;

public class HTTPRequest {
    private String method;
    private String path;
    private HashMap<String, String> headers;
    private String rawHeaders;
    private String version;

    public HTTPRequest(String method, String path, HashMap<String, String> headers, String rawHeaders, String version) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.rawHeaders = rawHeaders;
        this.version = version;
    }
    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public String getRawHeaders() {
        return rawHeaders;
    }

    public String getVersion() {
        return version;
    }
}
