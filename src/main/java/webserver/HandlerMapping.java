package webserver;

import webserver.handler.StaticFileHandler;
import webserver.http.HTTPMethod;
import webserver.handler.Handler;
import java.util.Map;

public class HandlerMapping {
    private final Map<RouteKey, Handler> handlerMap;
    private final StaticFileHandler staticFileHandler;

    public HandlerMapping(Map<RouteKey, Handler> handlerMap, StaticFileHandler staticFileHandler){
        this.handlerMap = handlerMap;
        this.staticFileHandler = staticFileHandler;
    }

    public Handler getProperHandler(HTTPMethod method, String path){
        Handler h = handlerMap.get(new RouteKey(method, path));
        if(h != null) {return h;}

        if(method == HTTPMethod.GET) {return staticFileHandler;}

        // 정의되어있지 않은 요청 예외 처리 필요
        throw new IllegalArgumentException("잘못된 요청입니다.");
    }
}
