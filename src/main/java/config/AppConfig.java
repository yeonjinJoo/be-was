package config;

import application.db.UserDatabase;
import application.handler.UserCreateHandler;
import application.handler.UserLoginController;
import application.service.UserService;
import webserver.HandlerMapping;
import webserver.RouteKey;
import webserver.Router;
import webserver.handler.Handler;
import webserver.handler.StaticFileHandler;
import webserver.http.HTTPMethod;
import webserver.session.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class AppConfig {

    private final UserDatabase userDatabase = new UserDatabase();
    private final UserService userService = new UserService(userDatabase);

    private final SessionManager sessionManager = new SessionManager();

    private final StaticFileHandler staticFileHandler = new StaticFileHandler();
    private final UserCreateHandler userCreateHandler = new UserCreateHandler(userService);
    private final UserLoginController userLoginController = new UserLoginController(userService, sessionManager);

    private final HandlerMapping handlerMapping = new HandlerMapping(handleMap(), staticFileHandler);

    public AppConfig(){
        router();
    }

    public Router router(){
        return new Router(handlerMapping, sessionManager);
    }

    // Handlers
    private Map<RouteKey, Handler> handleMap(){
        Map<RouteKey, Handler> map = new HashMap<>();
        map.put(new RouteKey(HTTPMethod.POST, "/user/create"), userCreateHandler);
        map.put(new RouteKey(HTTPMethod.POST, "/user/login"), userLoginController);
        return map;
    }
}
