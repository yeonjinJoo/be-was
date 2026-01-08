package webserver;

import webserver.http.HTTPMethod;
import webserver.handler.Handler;
import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;
import webserver.session.SessionManager;

public class Router {
    private final HandlerMapping handlerMapping;
    private final SessionManager sessionManager;

    public Router(HandlerMapping handlerMapping, SessionManager sessionManager) {
        this.handlerMapping = handlerMapping;
        this.sessionManager = sessionManager;
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
