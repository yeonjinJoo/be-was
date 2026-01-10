package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HTTPMethod;
import webserver.handler.Handler;
import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;
import webserver.interceptor.Interceptor;
import webserver.interceptor.InterceptorRegistry;

import java.util.List;

public class Dispatcher {
    private final HandlerMapping handlerMapping;
    private final InterceptorRegistry interceptorRegistry;

    public Dispatcher(HandlerMapping handlerMapping, InterceptorRegistry interceptorRegistry) {
        this.handlerMapping = handlerMapping;
        this.interceptorRegistry = interceptorRegistry;
    }

    public HTTPResponse dispatch(HTTPRequest request) {
        String path = request.getPath();
        HTTPMethod method = request.getMethod();

        try{
            Handler handler = handlerMapping.getProperHandler(method, path);

            HTTPResponse response = new HTTPResponse();
            List<Interceptor> interceptors = interceptorRegistry.getInterceptorsForPath(path);
            for (Interceptor interceptor : interceptors) {
                // preHandle이 false를 반환하면 즉시 중단하고 현재 response 반환
                if (!interceptor.preHandle(request, response)) {
                    return response;
                }
            }

            return handler.handle(request);

        } catch (IllegalStateException e){
            return HTTPResponse.conflict(e.getMessage());
        } catch (Exception e){
            return HTTPResponse.internalServerError();
        }
    }
}
