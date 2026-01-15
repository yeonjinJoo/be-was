package webserver.exception.webexception;

import webserver.http.HTTPStatus;

public class BadRequestException extends WebException {
    private static final String LOGIN_PAGE = "/login";
    private static final String MY_PAGE = "/mypage";
    private static final String ARTICLE_PAGE = "/article";

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

    public static BadRequestException missingChangePassword(){
        return new BadRequestException("새로운 비밀번호와 비밀번호 확인 모두 입력해 주세요.", MY_PAGE);
    }

    public static BadRequestException invalidChangePassword(){
        return new BadRequestException("새로운 비밀번호와 비밀번호 확인이 일치하지 않습니다.", MY_PAGE);
    }

    public static BadRequestException missingArticleImage(){
        return new BadRequestException("파일 첨부가 필요합니다.", ARTICLE_PAGE);
    }

    public static BadRequestException invalidImageType() {
        return new BadRequestException(
                "이미지 파일만 업로드할 수 있습니다.",
                ARTICLE_PAGE
        );
    }

    public static BadRequestException invalidImageExtension() {
        return new BadRequestException(
                "허용되지 않은 이미지 확장자입니다.",
                ARTICLE_PAGE
        );
    }

    public static BadRequestException emptyImageFile() {
        return new BadRequestException(
                "업로드된 파일이 비어 있습니다.",
                ARTICLE_PAGE
        );
    }

    public static BadRequestException imageSizeExceeded(int maxMB) {
        return new BadRequestException(
                "이미지 크기는 " + maxMB + "MB 이하만 가능합니다.",
                ARTICLE_PAGE
        );
    }
}
