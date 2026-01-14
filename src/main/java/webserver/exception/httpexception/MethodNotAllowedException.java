package webserver.exception.httpexception;

import webserver.exception.webexception.WebException;
import webserver.http.HTTPMethod;
import webserver.http.HTTPStatus;

public class MethodNotAllowedException extends HTTPException{
    public MethodNotAllowedException(String message) {
        super(HTTPStatus.METHOD_NOT_ALLOWED, message);
    }

    public static MethodNotAllowedException fromPath(String path, HTTPMethod method) {
        return new MethodNotAllowedException(
                String.format("해당 경로(%s)는 %s 메서드를 지원하지 않습니다.", path, method)
        );
    }
}
