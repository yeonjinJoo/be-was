package application.handler;

import session.CookieUtils;
import session.SessionManager;
import webserver.handler.DynamicHandler;
import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;

public class UserLogoutHandler extends DynamicHandler {
    private final SessionManager sessionManager;

    public UserLogoutHandler(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public HTTPResponse handle(HTTPRequest request) {
        String sid = request.getSid();

        sessionManager.invalidate(sid);
        HTTPResponse response = HTTPResponse.redirect("/index.html");
        response.addHeader("Set-Cookie", CookieUtils.buildExpireSidCookie(sid));
        return response;
    }
}
