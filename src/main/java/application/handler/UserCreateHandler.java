package application.handler;

import application.model.User;
import application.service.UserService;
import webserver.handler.DynamicHandler;
import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;

import java.security.InvalidParameterException;
import java.util.Map;

public class UserCreateHandler extends DynamicHandler {
    private final UserService userService;

    public UserCreateHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public HTTPResponse handle(HTTPRequest request) {
        User user = createUser(request);
        userService.create(user);
        return HTTPResponse.redirect("/index.html");
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

    private boolean isPresent(Map<String, String> qp, String key) {
        String v = qp.get(key);
        return v != null && !v.isBlank();
    }
}
