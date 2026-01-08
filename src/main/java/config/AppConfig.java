package config;

import application.db.UserDatabase;
import application.handler.UserHandler;
import application.service.UserService;
import webserver.HandlerMapping;
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
    private final UserHandler userHandler = new UserHandler(userService, userHandlerHandleList(), sessionManager);

    private final HandlerMapping handlerMapping = new HandlerMapping(handleMap());

    public AppConfig(){
        router();
    }

    public Router router(){
        return new Router(handlerMapping, sessionManager);
    }

    // Handlers
    private Map<String, Handler> handleMap(){
        Map<String, Handler> map = new HashMap<>();
        map.put("/user", userHandler);
        map.put("/", staticFileHandler); // fallback
        return map;
    }

    private Map<String, HTTPMethod> userHandlerHandleList(){
        Map<String, HTTPMethod> map = new HashMap<>();
        map.put("/user/create", HTTPMethod.POST);
        map.put("/user/login", HTTPMethod.POST);
        return map;
    }
}
