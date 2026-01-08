package webserver.handler;

import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;

public abstract class DynamicHandler implements Handler{
    public abstract HTTPResponse handle(HTTPRequest request);
}
