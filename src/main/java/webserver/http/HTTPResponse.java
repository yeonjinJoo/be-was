package webserver.http;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HTTPResponse {

    private static final Map<Integer, String> STATUS_MESSAGES = Map.of(
            200, "OK",
            404, "Not Found",
            500, "Internal Server Error"
    );

    private final int statusCode;
    private final String contentType;
    private final byte[] body;

    private HTTPResponse(int statusCode, String contentType, byte[] body) {
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.body = body;
    }

    public static HTTPResponse ok(String contentType, byte[] body) {
        return new HTTPResponse(200, contentType, body);
    }

    public static HTTPResponse notFound() {
        return new HTTPResponse(
                404,
                "text/html;charset=utf-8",
                "<h1>404 Not Found</h1>".getBytes(StandardCharsets.UTF_8)
        );
    }

    public static HTTPResponse internalServerError() {
        return new HTTPResponse(
                500,
                "text/html;charset=utf-8",
                "<h1>500 Internal Server Error</h1>".getBytes(StandardCharsets.UTF_8)
        );
    }

    public int getStatusCode() {
        return statusCode;
    }
    public String getStatusMessage() {
        return STATUS_MESSAGES.getOrDefault(statusCode, "");
    }
    public String getContentType() {
        return contentType;
    }
    public byte[] getBody() {
        return body;
    }
}
