package application.handler;

import application.model.User;
import webserver.handler.DynamicHandler;
import webserver.http.HTTPRequest;
import webserver.session.SessionManager;
import webserver.view.ModelAndView;

public class MainPageHandler extends DynamicHandler {
    private final SessionManager sessionManager;

    public MainPageHandler(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    // TODO: MainPage 내용 갈아끼우는거 구현하기. 특히 로그인 여부에 따라 file 자체가 달라지는 경우.
    @Override
    public ModelAndView handle(HTTPRequest request) {
        ModelAndView modelAndView = new ModelAndView(request.getPath());

        addTop(request.getSid(), modelAndView);
        return modelAndView;
    }

    private void addTop(String sid, ModelAndView modelAndView) {
        User user = sessionManager.getUser(sid);
        String replaceContent;
        if(user != null) {
            replaceContent = "nav_user.html";
            modelAndView.addObject("userName", user.getName());
        }
        else {
            replaceContent = "nav_guest.html";
        }

        modelAndView.addObject("userStatus", replaceContent);
    }
}
