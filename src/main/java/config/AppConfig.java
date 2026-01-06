package config;

import application.handler.UserHandler;
import webserver.HandlerMapping;
import webserver.Router;
import webserver.handler.Handler;
import webserver.handler.StaticFileHandler;
import java.util.List;

public class AppConfig {
    public Router router(){
        return new Router(handlerMapping());
    }

    private HandlerMapping handlerMapping(){
        return new HandlerMapping(handlersList());
    }

    // Handlers
    private List<Handler> handlersList(){
        return List.of(
                userHandler(),
                staticFileHandler()
        );
    }

    private StaticFileHandler staticFileHandler(){
        return new StaticFileHandler();
    }

    private UserHandler userHandler(){
        return new UserHandler();
    }
}
