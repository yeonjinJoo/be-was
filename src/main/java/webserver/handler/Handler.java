package webserver.handler;

import webserver.http.HTTPMethod;
import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;

public interface Handler {
    HTTPResponse handle(HTTPRequest request);
    boolean canHandle(HTTPMethod method, String path);
}
