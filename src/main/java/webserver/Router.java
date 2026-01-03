package webserver;

import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;

public class Router {

    public static HTTPResponse handle(HTTPRequest request) {
        return switch (request.getMethod()){
            case "GET" -> handleGetRequest(request);
//            case "POST" -> handlePostRequest(request);
            default -> null;
        };
    }

    private static HTTPResponse handleGetRequest(HTTPRequest request){
        String path = request.getPath();
        String resourcePath = path;

        if(path.equals("/")){
            return StaticFileServer.serve("/index.html");
        }

        if(path.equals("/registration")){
            resourcePath += "/index.html";
        }

        return StaticFileServer.serve(resourcePath);
    }
}
