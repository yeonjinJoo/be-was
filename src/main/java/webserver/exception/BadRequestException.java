package webserver.exception;

import webserver.http.HTTPStatus;

public class BadRequestException extends WebException{
    public BadRequestException(String message) {
        super(HTTPStatus.BAD_REQUEST, message);
    }

    public BadRequestException(String message, String code) {
        super(HTTPStatus.BAD_REQUEST, message, code);
    }

    public static BadRequestException invalidUserId(){
        return new BadRequestException("존재하지 않는 아이디입니다.회원 가입 하시겠습니까?", "userNotFound");
    }

    public static BadRequestException invalidPassword(){
        return new BadRequestException("비밀번호가 틀렸습니다.");
    }

    public static BadRequestException invalidParameters() {
        return new BadRequestException("입력은 4글자 이상이어야 합니다.");
    }

    public static BadRequestException missingParameters() {
        return new BadRequestException("요청 데이터가 충분하지 않습니다.");
    }
}
