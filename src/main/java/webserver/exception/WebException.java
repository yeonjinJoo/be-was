package webserver.exception;

import webserver.http.HTTPStatus;

// TODO: InternalServerException과 WebException으로 abstract class를 나누기
public abstract class WebException extends RuntimeException{
    private final HTTPStatus status;
    private final String code;

    protected WebException(HTTPStatus status, String message) {
        super(message);
        this.status = status;
        this.code = null;
    }

    protected WebException(HTTPStatus status, String message, String code) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public HTTPStatus getStatus() { return status; }

    public String getCode() {
        return code;
    }
}
