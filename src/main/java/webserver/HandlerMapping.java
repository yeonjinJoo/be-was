package webserver;

import webserver.exception.MethodNotAllowedException;
import webserver.exception.NotFoundException;
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
            return findHandlerInCandidates(directCandidates, path, method);
        }

        // 2. path의 패턴이 일치하는 경우 eg. /user/create/{id}와 /user/create/1의 경우 일치
        List<RouteKey> patternCandidates = patternLookup.stream()
                .filter(key -> key.matches(path))
                .toList();
        if(!patternCandidates.isEmpty()){
            return findHandlerInCandidates(patternCandidates, path, method);
        }

        // 3. 아무것도 일치하지 않는 경우 정적파일 확인
        if(method == HTTPMethod.GET){
            return staticFileHandler;
        }

        throw NotFoundException.pageNotFound(path);
    }

    private Handler findHandlerInCandidates(List<RouteKey> candidates, String path, HTTPMethod method) {
        return candidates.stream()
                .filter(key -> key.method() == method)
                .findFirst()
                .map(handlerMap::get)
                .orElseThrow(() -> MethodNotAllowedException.fromPath(path, method));
    }
}
