package application.handler;

import application.service.UserService;
import webserver.exception.webexception.BadRequestException;
import webserver.exception.webexception.ConflictException;
import webserver.handler.DynamicHandler;
import webserver.http.HTTPRequest;
import webserver.session.SessionManager;
import webserver.view.ModelAndView;
import java.util.Map;

public class UserChangeInfoHandler extends DynamicHandler {
    private static final String MY_PAGE = "/mypage";
    private final UserService userService;
    private final SessionManager sessionManager;

    public UserChangeInfoHandler(UserService userService, SessionManager sessionManager) {
        this.userService = userService;
        this.sessionManager = sessionManager;
    }

    @Override
    public ModelAndView handle(HTTPRequest request) {
        Map<String, String> bodyParams = request.getBodyParams();

        String userName = bodyParams.get("userName");
        checkUserName(userName);

        String newPassword = bodyParams.get("password");
        String confirmNewPassword = bodyParams.get("confirmNewPassword");
        checkPassword(newPassword, confirmNewPassword);

    }

    private void checkUserName(String userName) {
        if(userName != null){
            if(userService.isDuplicateName(userName)){
                throw ConflictException.duplicateUserName(MY_PAGE);
            }

            if(!userService.isValidLength(userName)){
                throw BadRequestException.invalidParameters(MY_PAGE);
            }
        }
    }

    private void checkPassword(String newPassword, String confirmNewPassword) {
        if(newPassword == null || confirmNewPassword == null){
            return;
        }


    }

//    public void
}
