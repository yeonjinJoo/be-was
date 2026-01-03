package application.handler;

import webserver.StaticFileServer;
import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;
import webserver.http.HTTPStatus;

public class RegistrationHandler implements Handler{
    @Override
    public HTTPResponse handle(HTTPRequest request) {
        String path = request.getPath();

        if(path.equals("/registration")){
            return StaticFileServer.serve(path + "/index.html");
        }

        HTTPStatus httpStatus = HTTPStatus.METHOD_NOT_ALLOWED;
        return new HTTPResponse(httpStatus.code(), httpStatus.meesage(), httpStatus.byteMessage());
    }
}
