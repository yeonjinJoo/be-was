package config;

import application.handler.UserHandler;
import webserver.HandlerMapping;
import webserver.Router;
import webserver.handler.Handler;
import webserver.handler.StaticFileHandler;

import java.util.HashMap;
import java.util.Map;

public class AppConfig {
    public Router router(){
        return new Router(handlerMapping());
    }

    private HandlerMapping handlerMapping(){
        return new HandlerMapping(handleMap());
    }

    // Handlers
    private Map<String, Handler> handleMap(){
        Map<String, Handler> map = new HashMap<>();
        map.put("/user", userHandler());
        map.put("/", staticFileHandler()); // fallback
        return map;
    }

    private StaticFileHandler staticFileHandler(){
        return new StaticFileHandler();
    }

    private UserHandler userHandler(){
        return new UserHandler();
    }
}
