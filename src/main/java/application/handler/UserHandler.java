package application.handler;

import application.service.UserService;
import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;

import java.util.Map;

public class UserHandler implements Handler{
    private final UserService userService = new UserService();

    @Override
    public HTTPResponse handle(HTTPRequest request) {
        String path = request.getPath();
        String method = request.getMethod();

        if(path.equals("/user/create")){
            switch (method) {
                case "GET":
                    Map<String, String> queryParams = request.getQueryParams();
                    if(isValidCreateParams(queryParams)) {
                        userService.create(queryParams.get("userId"),
                                           queryParams.get("password"),
                                           queryParams.get("name"),
                                           queryParams.get("email"));
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
