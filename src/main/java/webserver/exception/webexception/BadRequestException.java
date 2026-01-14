package webserver.exception.webexception;

import webserver.http.HTTPStatus;

public class BadRequestException extends WebException {
    private static final String LOGIN_PAGE = "/login/index.html";

    public BadRequestException(String message, String redirectPath) {
        super(HTTPStatus.BAD_REQUEST, message, redirectPath);
    }

    public static BadRequestException invalidUserId(){
        BadRequestException badRequestException = new BadRequestException("존재하지 않는 아이디입니다. 회원가입 하시겠습니까?", LOGIN_PAGE);
        badRequestException.addAttribute("isUserNotFound", true);
        return badRequestException;
    }

    public static BadRequestException invalidPassword(){
        return new BadRequestException("비밀번호가 틀렸습니다.", LOGIN_PAGE);
    }

    public static BadRequestException invalidParameters(String redirectPath) {
        return new BadRequestException("입력은 4글자 이상이어야 합니다.", redirectPath);
    }

    public static BadRequestException missingParameters() {
        return new BadRequestException("요청 데이터가 충분하지 않습니다.", LOGIN_PAGE);
    }
}
