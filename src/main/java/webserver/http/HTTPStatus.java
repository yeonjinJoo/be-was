package webserver.http;

public enum HTTPStatus {
    SUCCESS(200, "OK"),
    NOT_FOUND(404, "404 Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed");

    private final int code;
    private final String message;

    HTTPStatus(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int code(){return code;}
    public String meesage(){return message;}
}
