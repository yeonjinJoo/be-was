package webserver;

import webserver.handler.StaticFileHandler;
import webserver.http.HTTPMethod;
import webserver.handler.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerMapping {
    private final Map<String, ArrayList<RouteKey>> directLookup = new HashMap<>();
    private final List<RouteKey> patternLookup = new ArrayList<>();
    private final Map<RouteKey, Handler> handlerMap = new HashMap<>();

    private final StaticFileHandler staticFileHandler;

    public HandlerMapping(StaticFileHandler staticFileHandler){
        this.staticFileHandler = staticFileHandler;
    }

    public void register(HTTPMethod method, String path, Handler handler){
        RouteKey key = new RouteKey(method, path);

        handlerMap.put(key, handler);

        if(path.contains("{") && path.contains("}")){
            patternLookup.add(key);
        } else{
            directLookup.computeIfAbsent(path, k -> new ArrayList<>()).add(key);
        }
    }

    public Handler getProperHandler(HTTPMethod method, String path){
        // 1. 정확히 path가 일치하는 경우
        List<RouteKey> directCandidates = directLookup.get(path);
        if(directCandidates != null){
            return findHandlerInCandidates(directCandidates, method);
        }

        // 2. path의 패턴이 일치하는 경우 eg. /user/create/{id}와 /user/create/1의 경우 일치
        List<RouteKey> patternCandidates = patternLookup.stream()
                .filter(key -> key.matches(path))
                .toList();
        if(!patternCandidates.isEmpty()){
            return findHandlerInCandidates(patternCandidates, method);
        }

        // 3. 아무것도 일치하지 않는 경우 정적파일 확인
        if(method == HTTPMethod.GET){
            return staticFileHandler;
        }

        // TODO: 해당 요청을 처리할 수 있는 핸들러 없음 예외 처리 필요
        throw new IllegalArgumentException("잘못된 요청입니다.");
    }

    private Handler findHandlerInCandidates(List<RouteKey> candidates, HTTPMethod method) {
        return candidates.stream()
                .filter(key -> key.method() == method)
                .findFirst()
                .map(handlerMap::get)
                .orElseThrow(() -> new IllegalArgumentException("Method Not Allowed: " + method));
    }
}
