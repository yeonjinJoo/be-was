package webserver;

import application.handler.Handler;
import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;

public class Router {

    public static HTTPResponse route(HTTPRequest request) {
        String path = request.getPath();
        Handler handler = HandlerMapping.getProperHandler(path);

        try{
            return handler.handle(request);
        } catch (IllegalStateException e){
            return HTTPResponse.conflict(e.getMessage());
        }
    }
}
