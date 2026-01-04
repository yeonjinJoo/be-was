package webserver.http;

import java.nio.charset.StandardCharsets;

public class HTTPResponse {

    private int statusCode;
    private String statusMessage;
    private byte[] body;
    private String contentType;

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

    public static HTTPResponse notFound(){
        HTTPStatus httpStatus = HTTPStatus.NOT_FOUND;
        String message = httpStatus.meesage();
        return new HTTPResponse(httpStatus.code(), message, message.getBytes(StandardCharsets.UTF_8));
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
}
