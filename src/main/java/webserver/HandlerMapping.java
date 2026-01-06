package webserver;

import http.HTTPMethod;
import webserver.handler.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerMapping {
    private final Map<HTTPMethod, List<Handler>> handlerMap = new HashMap<>();

    public HandlerMapping(List<Handler> handlers){
        for(HTTPMethod httpMethod : HTTPMethod.values()){
            List<Handler> matchedHandlers = handlers.stream()
                    .filter(h -> h.canHandleMethod(httpMethod))
                    .toList();

            handlerMap.put(httpMethod, new ArrayList<>(matchedHandlers));
        }
    }

    public Handler getProperHandler(HTTPMethod method, String path){
        List<Handler> handlers = handlerMap.get(method);
        for( Handler handler : handlers ){
            if(handler.canHandle(method, path)){
                return handler;
            }
        }

        // 정의되어있지 않은 요청 예외 처리 필요
        throw new IllegalArgumentException("잘못된 요청입니다.");
    }
}
