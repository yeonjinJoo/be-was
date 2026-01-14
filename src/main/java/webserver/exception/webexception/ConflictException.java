package webserver.exception.webexception;

import webserver.http.HTTPStatus;

public class ConflictException extends WebException {
    public ConflictException(String message, String redirectPath) {
        super(HTTPStatus.CONFLICT, message, redirectPath);
    }

    public static ConflictException duplicateUserId(String redirectPath){
        return new ConflictException("이미 존재하는 아이디입니다.", redirectPath);
    }

    public static ConflictException duplicateUserName(String redirectPath){
        return new ConflictException("이미 존재하는 닉네임입니다.", redirectPath);
    }
}
