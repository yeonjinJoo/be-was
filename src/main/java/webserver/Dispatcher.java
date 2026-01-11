package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.BadRequestException;
import webserver.exception.ConflictException;
import webserver.exception.UnauthorizedException;
import webserver.exception.WebException;
import webserver.http.HTTPMethod;
import webserver.handler.Handler;
import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;
import webserver.interceptor.Interceptor;
import webserver.interceptor.InterceptorRegistry;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Dispatcher {
    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

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

            List<Interceptor> interceptors = interceptorRegistry.getInterceptorsForPath(path);
            for (Interceptor interceptor : interceptors) {
                interceptor.preHandle(request);
            }

            return handler.handle(request);

        } catch (WebException e){
            logger.warn("Web Error Occurred: {} {}", e.getStatus(), e.getMessage());
            return handleWebException(e, request);
        } catch (Exception e){
            logger.error("Internal System Error: ", e.getMessage());
            return HTTPResponse.internalServerError();
        }
    }

    public HTTPResponse handleWebException(WebException e, HTTPRequest request){
        if(e instanceof UnauthorizedException ||
                e instanceof BadRequestException ||
                e instanceof ConflictException){
            String encodedMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            return HTTPResponse.redirect("/login/index.html?error=true&message=" + encodedMessage);
        }

        return HTTPResponse.error(e.getStatus(), e.getMessage());
    }
}
