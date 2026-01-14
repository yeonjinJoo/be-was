//package application.handler;
//
//import application.service.UserService;
//import webserver.handler.DynamicHandler;
//import webserver.http.HTTPRequest;
//import webserver.session.SessionManager;
//import webserver.view.ModelAndView;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class UserChangeInfoHandler extends DynamicHandler {
//    private final UserService userService;
//    private final SessionManager sessionManager;
//
//    public UserChangeInfoHandler(UserService userService, SessionManager sessionManager) {
//        this.userService = userService;
//        this.sessionManager = sessionManager;
//    }
//
//    @Override
//    public ModelAndView handle(HTTPRequest request) {
//        Map<String, String> bodyParams = request.getBodyParams();
//
//        String userName = bodyParams.get("userName");
//
//    }
//}
