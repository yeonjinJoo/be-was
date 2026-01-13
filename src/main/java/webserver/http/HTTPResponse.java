package webserver.http;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HTTPResponse {

    private int statusCode = 200; // 기본값 OK
    private String statusMessage = "OK";
    private byte[] body = new byte[0];
    private String contentType = "text/html;charset=utf-8";
    private final Map<String, String> headers = new HashMap<>();

    public HTTPResponse(int statusCode, String statusMessage, byte[] body){
        this(statusCode, statusMessage, body, "text/html;charset=utf-8");
    }

    public HTTPResponse(int statusCode, String statusMessage, byte[] body, String contentType){
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.body = body;
        this.contentType = contentType;
    }

    public static HTTPResponse ok(String contentType, byte[] body){
        HTTPStatus httpStatus = HTTPStatus.SUCCESS;
        return new HTTPResponse(httpStatus.code(), httpStatus.meesage(), body, contentType);
    }

    public static HTTPResponse redirect(String location){
        HTTPStatus httpStatus = HTTPStatus.REDIRECT;
        HTTPResponse httpResponse = new HTTPResponse(httpStatus.code(), httpStatus.meesage(), new byte[0]);
        httpResponse.addHeader("Location", location);
        return httpResponse;
    }

    public static HTTPResponse error(HTTPStatus httpStatus, String message){
        return new HTTPResponse(httpStatus.code(), httpStatus.meesage(), message.getBytes(StandardCharsets.UTF_8));
    }

    public static HTTPResponse internalServerError(){
        HTTPStatus httpStatus = HTTPStatus.INTERNAL_SERVER_ERROR;
        String message = httpStatus.meesage();
        return new HTTPResponse(httpStatus.code(), message, message.getBytes(StandardCharsets.UTF_8));
    }

    public int getStatusCode() {
        return statusCode;
    }
    public String getStatusMessage() { return statusMessage; }
    public String getContentType() {
        return contentType;
    }
    public byte[] getBody() {
        return body;
    }
    public void addHeader(String key, String value){
        headers.put(key, value);
    }
    public Map<String, String> getHeaders() {
        return headers;
    }
}
