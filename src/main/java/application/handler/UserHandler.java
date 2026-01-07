package application.handler;

import application.model.User;
import application.service.UserService;
import http.HTTPMethod;
import http.HTTPRequest;
import http.HTTPResponse;
import webserver.handler.DynamicHandler;

import java.util.HashMap;
import java.util.Map;

public class UserHandler extends DynamicHandler {
    private final UserService userService = new UserService();
    private final Map<String, HTTPMethod> canHandleList = new HashMap<String, HTTPMethod>();

    public UserHandler() {
        canHandleList.put("/user/create", HTTPMethod.POST);
    }

    @Override
    public boolean canHandle(HTTPMethod method, String path) {
        if (canHandleList.containsKey(path) && canHandleList.get(path).equals(method)) {
            return true;
        }
        return false;
    }

    @Override
    public HTTPResponse handle(HTTPRequest request) {
        String path = request.getPath();
        HTTPMethod method = request.getMethod();

        if(path.equals("/user/create")){
            switch (method.toString()) {
                case "POST":
                    createUser(request);
                    return HTTPResponse.redirect("/index.html");
                default:
                    // 이 부분도 throw error로 변경하기
                    return HTTPResponse.methodNotAllowed();
            }
        }

        return HTTPResponse.notFound();
    }

    private void createUser(HTTPRequest request) {
        Map<String, String> bodyParams = request.getBodyParams();
        if(!isValidCreateParams(bodyParams)) {
            // illegal error 던지기. bad Request
        }
        User user = new User(bodyParams.get("userId"),
                bodyParams.get("password"),
                bodyParams.get("name"),
                bodyParams.get("email"));
        userService.create(user);
    }

    private boolean isValidCreateParams(Map<String, String> qp) {
        if (qp == null || qp.isEmpty()) return false;

        return isPresent(qp, "userId")
                && isPresent(qp, "password")
                && isPresent(qp, "name")
                && isPresent(qp, "email");
    }

    private boolean isPresent(Map<String, String> qp, String key) {
        String v = qp.get(key);
        return v != null && !v.isBlank();
    }
}
