package application.handler;

import application.model.User;
import application.service.UserService;
import webserver.http.HTTPMethod;
import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;
import webserver.handler.DynamicHandler;
import webserver.session.CookieUtils;
import webserver.session.SessionManager;

import java.security.InvalidParameterException;
import java.util.Map;

public class UserHandler extends DynamicHandler {
    private final UserService userService;
    private final Map<String, HTTPMethod> canHandleList;
    private final SessionManager sessionManager;

    public UserHandler(UserService userService, Map<String, HTTPMethod> canHandleList, SessionManager sessionManager) {
        this.userService = userService;
        this.canHandleList = canHandleList;
        this.sessionManager = sessionManager;
    }

    @Override
    public boolean canHandle(HTTPMethod method, String path) {
        return canHandleList.containsKey(path) && canHandleList.get(path).equals(method);
    }

    @Override
    public HTTPResponse handle(HTTPRequest request) {
        String path = request.getPath();
        HTTPMethod method = request.getMethod();

        if(path.equals("/user/create")){
            switch (method.toString()) {
                case "POST":
                    User user = createUser(request);
                    userService.create(user);
                    return HTTPResponse.redirect("/index.html");
                default: // 이 부분도 throw error로 변경하기
                    return HTTPResponse.methodNotAllowed();
            }
        }

        if(path.equals("/user/login")){
            switch (method.toString()) {
                case "POST":
                    Map<String, String> bodyParams = request.getBodyParams();
                    if(isValidLogin(bodyParams)) {
                        User user = userService.login(bodyParams.get("userId"), bodyParams.get("password"));
                        String sid = sessionManager.createSession(user);
                        HTTPResponse response = HTTPResponse.redirect("/index.html");
                        response.addHeader("Set-Cookie", CookieUtils.buildSetCookieSid(sid));
                        return response;
                    }
                default:
                    return HTTPResponse.methodNotAllowed();
            }
        }

        return HTTPResponse.notFound();
    }

    private User createUser(HTTPRequest request) {
        Map<String, String> bodyParams = request.getBodyParams();
        if(!isValidCreateParams(bodyParams)) {
            // illegal error 던지기. bad Request
            throw new InvalidParameterException("Invalid request parameters");
        }
        User user = new User(bodyParams.get("userId"),
                bodyParams.get("password"),
                bodyParams.get("name"),
                bodyParams.get("email"));
        return user;
    }

    private boolean isValidCreateParams(Map<String, String> qp) {
        if (qp == null || qp.isEmpty()) return false;

        return isPresent(qp, "userId")
                && isPresent(qp, "password")
                && isPresent(qp, "name")
                && isPresent(qp, "email");
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
