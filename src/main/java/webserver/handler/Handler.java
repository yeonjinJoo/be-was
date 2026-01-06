package webserver.handler;

import http.HTTPMethod;
import http.HTTPRequest;
import http.HTTPResponse;

public interface Handler {
    HTTPResponse handle(HTTPRequest request);
    boolean canHandle(HTTPMethod method, String path);
}
