package webserver.interceptor;

import webserver.exception.UnauthorizedException;
import webserver.session.SessionManager;
import webserver.http.HTTPRequest;

public class LoginCheckInterceptor implements Interceptor{
    private final SessionManager sessionManager;

    public LoginCheckInterceptor(SessionManager sessionManager){
        this.sessionManager = sessionManager;
    }
    @Override
    public void preHandle(HTTPRequest request) {
        String sid = request.getSid();
        if(sid != null && sessionManager.getUser(sid) != null){
            return;
        }
        throw UnauthorizedException.needLogin(request.getPath());
    }
}
