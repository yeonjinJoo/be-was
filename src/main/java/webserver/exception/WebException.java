package webserver.exception;

import webserver.http.HTTPStatus;

public abstract class WebException extends RuntimeException{
    private final HTTPStatus status;

    protected WebException(HTTPStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HTTPStatus getStatus() { return status; }
}
