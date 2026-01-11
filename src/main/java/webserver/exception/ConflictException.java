package webserver.exception;

import webserver.http.HTTPStatus;

public class ConflictException extends WebException {
    public ConflictException(String message) {
        super(HTTPStatus.CONFLICT, message);
    }

    public static ConflictException dupliacteUserId(){
        return new ConflictException("이미 존재하는 아이디입니다.");
    }
}
