package webserver.http;

import webserver.multipart.UploadedFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTTPRequest {
    private HTTPMethod method;
    private String path;
    private HashMap<String, String> queryParams;
    private HashMap<String, String> headers;
    private HashMap<String, String> bodyParams;
    private final byte[] rawBody;
    private final Map<String, List<UploadedFile>> files;
    private String rawHeaders;
    private String version;
    private String sid;

    public HTTPRequest(HTTPMethod method,
                       String path,
                       HashMap<String, String> queryParams,
                       HashMap<String, String> headers,
                       HashMap<String, String> bodyParams,
                       byte[] rawBody,
                       Map<String, List<UploadedFile>> files,
                       String rawHeaders,
                       String version,
                       String sid) {
        this.method = method;
        this.path = path;
        this.queryParams = queryParams;
        this.headers = headers;
        this.bodyParams = bodyParams;
        this.rawBody = rawBody;
        this.files = files;
        this.rawHeaders = rawHeaders;
        this.version = version;
        this.sid = sid;
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

    public byte[] getRawBody() {
        return rawBody;
    }

    public Map<String, List<UploadedFile>> getFiles() {
        return files;
    }

    public String getRawHeaders() {
        return rawHeaders;
    }

    public HashMap<String, String> getBodyParams() { return bodyParams; }

    public String getVersion() {
        return version;
    }

    public String getSid() { return sid; }

    public String getHeader(String key) {
        return headers.get(key.toLowerCase());
    }

    public UploadedFile getFirstFile(String name) {
        List<UploadedFile> list = files.get(name);
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }

    // MultipartParser가 텍스트 필드를 여기로 넣어주도록
    public void putBodyParam(String key, String value) {
        bodyParams.put(key, value);
    }
}
