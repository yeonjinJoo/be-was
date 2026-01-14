package webserver.exception.httpexception;

import webserver.exception.webexception.WebException;
import webserver.http.HTTPStatus;

public class InternalServerException extends HTTPException {
    public InternalServerException(String message) {
        super(HTTPStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static InternalServerException fileError(){
        return new InternalServerException("서버에서 파일 처리 중 오류가 발생했습니다.");
    }
}
