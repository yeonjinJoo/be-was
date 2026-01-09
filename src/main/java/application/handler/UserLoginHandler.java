package application.handler;

import application.model.User;
import application.service.UserService;
import webserver.handler.DynamicHandler;
import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;
import session.CookieUtils;
import session.SessionManager;

import java.util.Map;

public class UserLoginHandler extends DynamicHandler {

    private final UserService userService;
    private final SessionManager sessionManager;

    public UserLoginHandler(UserService userService, SessionManager sessionManager){
        this.userService = userService;
        this.sessionManager = sessionManager;
    }

    @Override
    public HTTPResponse handle(HTTPRequest request) {
        Map<String, String> bodyParams = request.getBodyParams();
        if(!isValidLogin(bodyParams)) {
            throw new IllegalStateException("Parameter가 잘못되었습니다.");
        }

        User user = userService.login(bodyParams.get("userId"), bodyParams.get("password"));
        String sid = sessionManager.createSession(user);
        HTTPResponse response = HTTPResponse.redirect("/main");
        response.addHeader("Set-Cookie", CookieUtils.buildSetCookieSid(sid));
        return response;
    }

    private boolean isValidLogin(Map<String, String> qp) {
        if (qp == null || qp.isEmpty()) return false;
        return isPresent(qp, "userId")
                && isPresent(qp, "password");
    }

    private boolean isPresent(Map<String, String> qp, String key) {
        String v = qp.get(key);
        return v != null && !v.isBlank();
    }
}
