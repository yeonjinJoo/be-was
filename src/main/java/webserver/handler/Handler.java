package webserver.handler;

import webserver.http.HTTPRequest;
import webserver.view.ModelAndView;

public interface Handler {
    ModelAndView handle(HTTPRequest request);
}
