package webserver.exception;

import webserver.http.HTTPStatus;

public class BadRequestException extends WebException{
    public BadRequestException(String message) {
        super(HTTPStatus.BAD_REQUEST, message);
    }

    public static BadRequestException missingParameters(){
        return new BadRequestException("요청 데이터가 충분하지 않습니다. 모든 필드를 입력해주세요.");
    }

    public static BadRequestException invalidParameters(String parameter){
        return new BadRequestException(parameter + "의 형식이 올바르지 않습니다.");
    }

    public static BadRequestException invalidLogin(){
        return new BadRequestException("아이디 또는 비밀번호가 잘못됐습니다. 다시 로그인해주세요.");
    }
}
