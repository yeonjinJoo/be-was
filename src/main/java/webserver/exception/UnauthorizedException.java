package webserver.exception;

import webserver.http.HTTPStatus;

public class UnauthorizedException extends WebException {
    public UnauthorizedException(String message) {
        super(HTTPStatus.UNAUTHORIZED, message);
    }

    public static UnauthorizedException needLogin(String path){
        return new UnauthorizedException(
                String.format("해당 경로(%s)에는 로그인이 필요합니다.", path)
        );
    }
}