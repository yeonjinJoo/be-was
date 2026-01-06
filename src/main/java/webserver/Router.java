package webserver;

import http.HTTPMethod;
import webserver.handler.Handler;
import http.HTTPRequest;
import http.HTTPResponse;

public class Router {
    private final HandlerMapping handlerMapping;
    public Router(HandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    public HTTPResponse route(HTTPRequest request) {
        String path = request.getPath();
        HTTPMethod method = request.getMethod();

        try{
            Handler handler = handlerMapping.getProperHandler(method, path);
            return handler.handle(request);
        } catch (IllegalStateException e){
            return HTTPResponse.conflict(e.getMessage());
        }
    }
}
