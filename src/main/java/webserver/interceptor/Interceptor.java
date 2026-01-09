package webserver.interceptor;

import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;

public interface Interceptor {
    // 핸들러 실행 전 검사. false를 리턴하면 실행 중단
    boolean preHandle(HTTPRequest request, HTTPResponse response);
}
