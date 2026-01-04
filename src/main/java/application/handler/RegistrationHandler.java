package application.handler;

import webserver.StaticFileServer;
import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;
import webserver.http.HTTPStatus;

public class RegistrationHandler implements Handler{
    @Override
    public HTTPResponse handle(HTTPRequest request) {
        String path = request.getPath();
        String method = request.getMethod();

        if(path.equals("/registration")){
            switch (method) {
                case "GET": return StaticFileServer.serve(path + "/index.html");
            }
        }

        return HTTPResponse.methodNotAllowed();
    }
}
