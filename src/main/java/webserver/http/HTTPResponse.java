package webserver.http;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HTTPResponse {

    private int statusCode = 200; // 기본값 OK
    private String statusMessage = "OK";
    private byte[] body = new byte[0];
    private String contentType = "text/html;charset=utf-8";
    private final Map<String, String> headers = new HashMap<>();

    public HTTPResponse() {}

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

    public static HTTPResponse badRequest(){
        HTTPStatus httpStatus = HTTPStatus.BAD_REQUEST;
        String message = httpStatus.meesage();
        return new HTTPResponse(httpStatus.code(), message, message.getBytes(StandardCharsets.UTF_8));
    }

    public static HTTPResponse notFound(){
        HTTPStatus httpStatus = HTTPStatus.NOT_FOUND;
        String message = httpStatus.meesage();
        return new HTTPResponse(httpStatus.code(), message, message.getBytes(StandardCharsets.UTF_8));
    }

    public static HTTPResponse conflict(String errorMessage){
        HTTPStatus httpStatus = HTTPStatus.CONFLICT;
        return new HTTPResponse(httpStatus.code(), httpStatus.meesage(), errorMessage.getBytes(StandardCharsets.UTF_8));
    }

    public static HTTPResponse internalServerError(){
        HTTPStatus httpStatus = HTTPStatus.INTERNAL_SERVER_ERROR;
        String message = httpStatus.meesage();
        return new HTTPResponse(httpStatus.code(), message, message.getBytes(StandardCharsets.UTF_8));
    }

    public static HTTPResponse methodNotAllowed(){
        HTTPStatus httpStatus = HTTPStatus.METHOD_NOT_ALLOWED;
        String message = httpStatus.meesage();
        return new HTTPResponse(httpStatus.code(), message, message.getBytes(StandardCharsets.UTF_8));
    }

    public void setStatus(HTTPStatus status) {
        this.statusCode = status.code();
        this.statusMessage = status.meesage(); // 오타(meesage)는 기존 코드 유지함
    }

    public void setRedirect(String location) {
        setStatus(HTTPStatus.REDIRECT);
        addHeader("Location", location);
        this.body = new byte[0];
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
