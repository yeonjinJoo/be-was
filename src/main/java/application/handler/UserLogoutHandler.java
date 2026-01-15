package application.handler;

import webserver.session.CookieUtils;
import webserver.session.SessionManager;
import webserver.handler.DynamicHandler;
import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;
import webserver.view.ModelAndView;

public class UserLogoutHandler extends DynamicHandler {
    private final SessionManager sessionManager;

    public UserLogoutHandler(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public ModelAndView handle(HTTPRequest request) {
        String sid = request.getSid();

        sessionManager.invalidate(sid);
        ModelAndView modelAndView = new ModelAndView("redirect:/");
        modelAndView.addHeader("Set-Cookie", CookieUtils.buildExpireSidCookie(sid));
        return modelAndView;
    }
}
