package webserver.handler;

import webserver.http.HTTPRequest;
import webserver.view.ModelAndView;

public abstract class DynamicHandler implements Handler{
    public abstract ModelAndView handle(HTTPRequest request);
}
