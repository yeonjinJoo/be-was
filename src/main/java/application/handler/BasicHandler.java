package application.handler;

import webserver.StaticFileServer;
import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;

public class BasicHandler implements Handler{
    @Override
    public HTTPResponse handle(HTTPRequest request) {
        String path = request.getPath();
        if(path.equals("/")){ path += "/index.html"; }

        return StaticFileServer.serve(path);
    }
}
