package application.handler;

import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;

public interface Handler {
    HTTPResponse handle(HTTPRequest request);
}
