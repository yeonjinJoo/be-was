package webserver.exception.httpexception;

import webserver.http.HTTPStatus;

public abstract class HTTPException extends RuntimeException {
    private final HTTPStatus status;
    public HTTPException(HTTPStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HTTPStatus getStatus() { return status; }

}
