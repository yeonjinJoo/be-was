package webserver.exception;

import webserver.http.HTTPStatus;

public class NotFoundException extends WebException {
    public NotFoundException(String message) {
        super(HTTPStatus.NOT_FOUND, message);
    }

    public static NotFoundException pageNotFound(String path){
        return new NotFoundException("존재하지 않는 경로입니다: " + path);
    }
}
