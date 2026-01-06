package webserver;

import http.HTTPMethod;
import webserver.handler.Handler;
import java.util.Map;

public class HandlerMapping {
    private final Map<String, Handler> handlerMap;

    public HandlerMapping(Map<String, Handler> handlerMap){
        this.handlerMap = handlerMap;
    }

    public Handler getProperHandler(HTTPMethod method, String path){
        String key = topLevelPath(path);

        Handler h = handlerMap.get(key);
        if(h != null && h.canHandle(method, path)) {return h;}

        Handler fallback = handlerMap.get("/");
        if(fallback != null && fallback.canHandle(method, path)) {return fallback;}

        // 정의되어있지 않은 요청 예외 처리 필요
        throw new IllegalArgumentException("잘못된 요청입니다.");
    }

    private static String topLevelPath(String path) {
        if (path == null || path.isBlank()) return "/";

        if (!path.startsWith("/")) path = "/" + path;
        if (path.equals("/")) return "/";

        int secondSlash = path.indexOf('/', 1);
        return (secondSlash == -1) ? path : path.substring(0, secondSlash);
    }
}
