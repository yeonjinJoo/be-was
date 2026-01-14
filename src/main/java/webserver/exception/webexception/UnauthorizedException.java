package webserver.exception.webexception;

import webserver.http.HTTPStatus;

public class UnauthorizedException extends WebException {
    private static final String LOGIN_PAGE = "/login/index.html";

    public UnauthorizedException(String message, String redirectPath) {
        super(HTTPStatus.UNAUTHORIZED, message, redirectPath);
    }

    public static UnauthorizedException needLogin(String path){
        String message = String.format("해당 경로(%s)에는 로그인이 필요합니다.", path);
        return new UnauthorizedException(message, LOGIN_PAGE);
    }
}