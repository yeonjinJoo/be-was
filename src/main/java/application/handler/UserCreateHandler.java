package application.handler;

import application.model.User;
import application.service.UserService;
import webserver.exception.webexception.BadRequestException;
import webserver.handler.DynamicHandler;
import webserver.http.HTTPRequest;
import webserver.view.ModelAndView;

import java.util.Map;

public class UserCreateHandler extends DynamicHandler {
    private static final String REGISTRATION_URL = "/registrationl";
    private final UserService userService;

    public UserCreateHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ModelAndView handle(HTTPRequest request) {
        User user = createUser(request);
        userService.create(user);

        return new ModelAndView("redirect:/login");
    }

    private User createUser(HTTPRequest request) {
        Map<String, String> bodyParams = request.getBodyParams();
        validatePresence(bodyParams);

        User user = new User(bodyParams.get("userId"),
                bodyParams.get("password"),
                bodyParams.get("name"),
                bodyParams.get("email"));

        return user;
    }

    private void validatePresence(Map<String, String> qp) {
        if (!isPresent(qp, "userId")
                || !isPresent(qp, "password")
                || !isPresent(qp, "name")
                || !isPresent(qp, "email")) {
            throw BadRequestException.missingParameters();
        }
    }

    private boolean isPresent(Map<String, String> qp, String key) {
        String v = qp.get(key);
        return v != null && !v.isBlank();
    }
}
