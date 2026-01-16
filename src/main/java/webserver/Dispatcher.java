package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.httpexception.HTTPException;
import webserver.exception.httpexception.NotFoundException;
import webserver.exception.webexception.WebException;
import webserver.http.HTTPMethod;
import webserver.handler.Handler;
import webserver.http.HTTPRequest;
import webserver.http.HTTPResponseWriter;
import webserver.interceptor.Interceptor;
import webserver.interceptor.InterceptorRegistry;
import webserver.view.ModelAndView;
import webserver.view.View;
import webserver.view.ViewResolver;
import java.io.DataOutputStream;
import java.util.List;

public class Dispatcher {
    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    private final HandlerMapping handlerMapping;
    private final InterceptorRegistry interceptorRegistry;

    public Dispatcher(HandlerMapping handlerMapping, InterceptorRegistry interceptorRegistry) {
        this.handlerMapping = handlerMapping;
        this.interceptorRegistry = interceptorRegistry;
    }

    public void dispatch(HTTPRequest request,
                                 DataOutputStream dos,
                                 HTTPResponseWriter writer) {
        String path = request.getPath();
        HTTPMethod method = request.getMethod();

        try{
            List<Interceptor> interceptors = interceptorRegistry.getInterceptorsForPath(path);
            for (Interceptor interceptor : interceptors) {
                interceptor.preHandle(request);
            }

            Handler handler = handlerMapping.getProperHandler(method, path);

            ModelAndView modelAndView = handler.handle(request);
            View view = ViewResolver.resolve(modelAndView.getViewName());
            if (view == null) {throw NotFoundException.pageNotFound(modelAndView.getViewName());}
            view.render(request.getVersion(), modelAndView.getModel(), modelAndView.getHeaders(), dos, writer);

            render(request.getVersion(), modelAndView, dos, writer);

        } catch (WebException e) {
            logger.warn("Web Error Occurred: {} {}", e.getStatus(), e.getMessage());
            render(request.getVersion(), handleWebException(e), dos, writer);
        } catch (HTTPException e) {
            logger.error("HTTP Error Occurred: {} {}", e.getStatus(), e.getMessage());
            render(request.getVersion(), handleHTTPException(e), dos, writer);
        } catch (Exception e){
            logger.error("Internal System Error: ", e.getMessage());
            e.printStackTrace();
        }
    }

    public ModelAndView handleWebException(WebException e) {
        String redirectPath = e.getRedirectPath();
        return buildErrorRedirectMav(redirectPath, e);
    }

    private ModelAndView buildErrorRedirectMav(String path, WebException e) {
        ModelAndView modelAndView = new ModelAndView(path);
        e.fillModel(modelAndView.getModel());

        return modelAndView;
    }

    public ModelAndView handleHTTPException(HTTPException e){
        ModelAndView modelAndView = new ModelAndView("/error/index.html");
        modelAndView.addObject("errorCode", e.getStatus().code());
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }

    private void render(String version,
                        ModelAndView mav,
                        DataOutputStream dos,
                        HTTPResponseWriter writer) {
        try{
            View view = ViewResolver.resolve(mav.getViewName());
            view.render(version, mav.getModel(), mav.getHeaders(), dos, writer);
        } catch (Exception e) {
            logger.error("Final Rendering Error: ", e);
        }
    }
}
