package webserver;

public class ApiHandler {

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
            return StaticFileHandler.serve("/index.html");
        }

        if(path.equals("/registration")){
            resourcePath += "/index.html";
        }

        return StaticFileHandler.serve(resourcePath);
    }
}
