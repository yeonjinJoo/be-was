package webserver.interceptor;

import application.model.User;
import session.SessionManager;
import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;

public class LoginCheckInterceptor implements Interceptor{
    private final SessionManager sessionManager;

    public LoginCheckInterceptor(SessionManager sessionManager){
        this.sessionManager = sessionManager;
    }
    @Override
    public boolean preHandle(HTTPRequest request, HTTPResponse response) {
        String sid = request.getSid();
        if(sid != null){
            User user = sessionManager.getUser(sid);
            if(user != null){
                return true;
            }
        }
        response.setRedirect("/login/index.html");
        return false;
    }
}
