package webserver.handler;

import webserver.view.ModelAndView;
import webserver.http.HTTPRequest;

import java.io.*;

public class StaticFileHandler implements Handler {

    public ModelAndView handle(HTTPRequest request) {
        return new ModelAndView(request.getPath());
    }
}
