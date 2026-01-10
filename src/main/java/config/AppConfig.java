package config;

import db.UserDatabase;
import application.handler.UserCreateHandler;
import application.handler.UserLoginHandler;
import application.handler.UserLogoutHandler;
import application.service.UserService;
import webserver.HandlerMapping;
import webserver.RouteKey;
import webserver.Dispatcher;
import webserver.handler.Handler;
import webserver.handler.StaticFileHandler;
import webserver.http.HTTPMethod;
import webserver.session.SessionManager;
import webserver.interceptor.InterceptorRegistry;
import webserver.interceptor.LoginCheckInterceptor;

import java.util.HashMap;
import java.util.Map;

public class AppConfig {

    private final UserDatabase userDatabase = new UserDatabase();
    private final UserService userService = new UserService(userDatabase);

    private final SessionManager sessionManager = new SessionManager();

    private final StaticFileHandler staticFileHandler = new StaticFileHandler();
    private final UserCreateHandler userCreateHandler = new UserCreateHandler(userService);
    private final UserLoginHandler userLoginHandler = new UserLoginHandler(userService, sessionManager);
    private final UserLogoutHandler userLogoutHandler = new UserLogoutHandler(sessionManager);

    private final LoginCheckInterceptor loginCheckInterceptor = new LoginCheckInterceptor(sessionManager);

    private final HandlerMapping handlerMapping = new HandlerMapping(handleMap(), staticFileHandler);

    private final InterceptorRegistry interceptorRegistry = configureInterceptors();

    public AppConfig() {
        dispatcher();
    }

    public Dispatcher dispatcher() {
        return new Dispatcher(handlerMapping, interceptorRegistry);
    }

    private InterceptorRegistry configureInterceptors() {
        InterceptorRegistry registry = new InterceptorRegistry();

        // 로그인 체크 인터셉터 등록 및 경로 설정
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/mypage/**", "/user/logout"); // 로그인이 필요한 페이지들

        return registry;
    }

    // Handlers
    private Map<RouteKey, Handler> handleMap() {
        Map<RouteKey, Handler> map = new HashMap<>();
        map.put(new RouteKey(HTTPMethod.POST, "/user/create"), userCreateHandler);
        map.put(new RouteKey(HTTPMethod.POST, "/user/login"), userLoginHandler);
        map.put(new RouteKey(HTTPMethod.POST, "/user/logout"), userLogoutHandler);
        return map;
    }
}
