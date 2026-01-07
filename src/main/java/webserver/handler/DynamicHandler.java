package webserver.handler;

import webserver.http.HTTPMethod;
import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;

public abstract class DynamicHandler implements Handler{
    public abstract boolean canHandle(HTTPMethod method, String path);
    public abstract HTTPResponse handle(HTTPRequest request);
}
