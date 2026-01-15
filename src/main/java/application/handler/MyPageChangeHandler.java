package application.handler;

import application.model.User;
import application.service.UserService;
import webserver.handler.DynamicHandler;
import webserver.http.HTTPRequest;
import webserver.session.SessionManager;
import webserver.view.ModelAndView;
import java.util.Map;
import java.util.Optional;

public class MyPageChangeHandler extends DynamicHandler {
    private static final String MY_PAGE = "/mypage";
    private final UserService userService;
    private final SessionManager sessionManager;

    public MyPageChangeHandler(UserService userService,  SessionManager sessionManager) {
        this.userService = userService;
        this.sessionManager = sessionManager;
    }

    @Override
    public ModelAndView handle(HTTPRequest request) {
        Map<String, String> bodyParams = request.getBodyParams();

        String userName = bodyParams.get("userName");
        String newPassword = bodyParams.get("newPassword");
        String confirmNewPassword = bodyParams.get("confirmNewPassword");

        User user = sessionManager.getUser(request.getSid());

        if (userName == null && newPassword == null && confirmNewPassword == null) {
            return new ModelAndView(MY_PAGE);
        }

        Optional<User> newInfoUser = userService.changeProfile(user.getId(), userName, newPassword, confirmNewPassword);
        if (newInfoUser.isPresent()) {
           sessionManager.setUser(request.getSid(), newInfoUser.get());
        }

        return new ModelAndView("redirect:/index.html");
    }
}

