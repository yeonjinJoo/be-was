package application.handler;

import application.model.User;
import application.service.UserService;
import webserver.exception.webexception.BadRequestException;
import webserver.handler.DynamicHandler;
import webserver.http.HTTPRequest;
import webserver.session.CookieUtils;
import webserver.session.SessionManager;
import webserver.view.ModelAndView;

import java.util.Map;

public class UserLoginHandler extends DynamicHandler {

    private final UserService userService;
    private final SessionManager sessionManager;

    public UserLoginHandler(UserService userService, SessionManager sessionManager){
        this.userService = userService;
        this.sessionManager = sessionManager;
    }

    @Override
    public ModelAndView handle(HTTPRequest request) {
        Map<String, String> bodyParams = request.getBodyParams();
        if(!isValidLogin(bodyParams)) {
            throw BadRequestException.missingParameters();
        }

        User user = userService.login(bodyParams.get("userId"), bodyParams.get("password"));
        String sid = sessionManager.createSession(user);
        ModelAndView modelAndView = new ModelAndView("redirect:/");
        modelAndView.addHeader("Set-Cookie", CookieUtils.buildSetCookieSid(sid));
        return modelAndView;
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
