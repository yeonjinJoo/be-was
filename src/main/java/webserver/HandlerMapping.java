package webserver;

import application.handler.BasicHandler;
import application.handler.Handler;
import application.handler.RegistrationHandler;
import application.handler.UserHandler;

import java.util.Map;

public class HandlerMapping {
    private static final RegistrationHandler registrationHandler = new RegistrationHandler();
    private static final UserHandler userHandler = new UserHandler();
    private static final BasicHandler basicHandler = new BasicHandler();

    private static final Map<String, Handler> API_HANDLERS = Map.of(
            "/registration", registrationHandler,
            "/user", userHandler
    );

    public static Handler getProperHandler(String path){
        String topLevelPath = extractTopLevelPath(path);

        Handler handler = API_HANDLERS.get(topLevelPath);
        if (handler == null) {
            handler = basicHandler;
        }

        return handler;
    }

    private static String extractTopLevelPath(String path) {
        if (path == null || path.isEmpty()) return "/";

        if (!path.startsWith("/")) path = "/" + path;

        if ("/".equals(path)) return "/";

        int secondSlash = path.indexOf('/', 1);

        return (secondSlash == -1) ? path : path.substring(0, secondSlash);
    }
}
