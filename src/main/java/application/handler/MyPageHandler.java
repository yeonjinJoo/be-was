package application.handler;

import application.model.User;
import webserver.handler.DynamicHandler;
import webserver.http.HTTPRequest;
import webserver.session.SessionManager;
import webserver.view.ModelAndView;

public class MyPageHandler extends DynamicHandler {
    private final SessionManager sessionManager;

    public MyPageHandler(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public ModelAndView handle(HTTPRequest request) {
        ModelAndView modelAndView = new ModelAndView(request.getPath());
        User user = sessionManager.getUser(request.getSid());
        modelAndView.addObject("userName", user.getName());
        return modelAndView;
    }
}
