package webserver.exception.httpexception;

import webserver.exception.webexception.WebException;
import webserver.http.HTTPStatus;

public class NotFoundException extends HTTPException {
    public NotFoundException(String message) {
        super(HTTPStatus.NOT_FOUND, message);
    }

    public static NotFoundException pageNotFound(String path){
        return new NotFoundException("요청하신 경로를 처리할 수 없거나 존재하지 않습니다: " + path);
    }
}
