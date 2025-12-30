package webserver;

public class HTTPResponse {
    private int statusCode;
    private String contentType;
    private byte[] body;

    public HTTPResponse(int statusCode, String contentType, byte[] body) {
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.body = body;
    }

    // HTTPResponse for error
    public HTTPResponse(int statusCode, byte[] body) {
        this.statusCode = statusCode;
        this.contentType = "text/html;charset=utf-8";
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }
    public String getContentType() {
        return contentType;
    }
    public byte[] getBody() {
        return body;
    }
}
