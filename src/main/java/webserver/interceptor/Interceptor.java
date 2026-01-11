package webserver.interceptor;

import webserver.http.HTTPRequest;

public interface Interceptor {
    // 핸들러 실행 전 검사
    void preHandle(HTTPRequest request);
}
