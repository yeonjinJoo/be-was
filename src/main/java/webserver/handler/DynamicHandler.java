package webserver.handler;

import http.HTTPMethod;
import http.HTTPRequest;
import http.HTTPResponse;

public abstract class DynamicHandler implements Handler{
    public abstract boolean canHandle(HTTPMethod method, String path);
    public abstract HTTPResponse handle(HTTPRequest request);
    public boolean canHandleMethod(HTTPMethod method){
        return true;
    }
}
