package webserver;

import webserver.http.HTTPMethod;

public record RouteKey(HTTPMethod method, String pathPattern) {
    public boolean matches(String requestPath) {
        // {id} 같은 부분을 "슬래시를 제외한 모든 문자" 패턴으로 바꾼다
        // "/user/{id}" -> "/user/[^/]+"
        String regex = pathPattern.replaceAll("\\{[^/]+\\}", "[^/]+");

        // 전체 경로가 일치하는지 정규식으로 확인
        return requestPath.matches(regex);
    }
}
