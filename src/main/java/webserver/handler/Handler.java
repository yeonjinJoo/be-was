package application.handler;

import http.HTTPRequest;
import http.HTTPResponse;

public interface Handler {
    HTTPResponse handle(HTTPRequest request);
}
