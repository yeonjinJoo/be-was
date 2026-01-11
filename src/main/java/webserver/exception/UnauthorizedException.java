package webserver.exception;

import webserver.http.HTTPStatus;

public class UnauthorizedException extends WebException {
    public UnauthorizedException(String message) {
        super(HTTPStatus.UNAUTHORIZED, message);
    }

    public static UnauthorizedException needLogin(){
        return new UnauthorizedException("로그인이 필요합니다.");
    }
}
