package application.handler;

import application.service.UserService;
import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;

import java.util.HashMap;

public class UserHandler implements Handler{
    private final UserService userService = new UserService();

    @Override
    public HTTPResponse handle(HTTPRequest request) {
        String path = request.getPath();
        String method = request.getMethod();

        if(path.equals("/user/create")){
            switch (method) {
                case "GET":
                    if(isValidCreateParams(request.getQueryParams())) {
                        userService.createUser();
                        // 성공 시 redirect to main page
                        return HTTPResponse.redirect("/index.html");
                    }
                    else
                        return HTTPResponse.badRequest();
                default:
                    return HTTPResponse.methodNotAllowed();
            }
        }

        return HTTPResponse.notFound();
    }

    private boolean isValidCreateParams(HashMap<String, String> qp) {
        if (qp == null || qp.isEmpty()) return false;

        return isPresent(qp, "userId") && isPresent(qp, "password") && isPresent(qp, "name");
    }

    private boolean isPresent(HashMap<String, String> qp, String key) {
        String v = qp.get(key);
        return v != null && !v.isBlank();
    }
}
