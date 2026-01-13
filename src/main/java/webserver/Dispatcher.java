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
import webserver.http.HTTPResponseWriter;
import webserver.interceptor.Interceptor;
import webserver.interceptor.InterceptorRegistry;
import webserver.view.ModelAndView;
import webserver.view.View;
import webserver.view.ViewResolver;
import java.io.DataOutputStream;
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

            // TODO: StaticHandler 다시 사용하는 방안으로 고민해보기. 현재 ViewResolver에서 Static File 처리 하는데 그러면 Static은 ModelAndView가 안생긴다.
            ModelAndView modelAndView = handler.handle(request);

            render(request.getVersion(), modelAndView, dos, writer);

        } catch (WebException e){
            logger.warn("Web Error Occurred: {} {}", e.getStatus(), e.getMessage());
            render(request.getVersion(), handleWebException(e), dos, writer);
        } catch (Exception e){
            // TODO: /error/500.html static file로 만들기
            logger.error("Internal System Error: ", e.getMessage());
            render(request.getVersion(), new ModelAndView("redirect:/error/500.html"), dos, writer);
        }
    }

    public ModelAndView handleWebException(WebException e) {
        String redirectPath = getRedirectPath(e);

        if (redirectPath != null) {
            return buildErrorRedirectMav(redirectPath, e);
        }

        // 리다이렉트가 아닌 경우
        // TODO: /error/default html 생성
        ModelAndView mav = new ModelAndView("/error/default");
        mav.addObject("errorCode", e.getStatus().code());
        mav.addObject("message", e.getMessage());
        return mav;
    }

    private String getRedirectPath(WebException e) {
        if (e instanceof UnauthorizedException ||
                e instanceof BadRequestException) {
            return "/login/index.html";
        }
        if (e instanceof ConflictException) {
            return "/registration/index.html";
        }
        return null;
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


    private ModelAndView buildErrorRedirectMav(String path, WebException e) {
        StringBuilder url = new StringBuilder(path);
        url.append("?error=true&message=")
                .append(URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));

        if (e.getCode() != null) {
            url.append("&code=")
                    .append(URLEncoder.encode(e.getCode(), StandardCharsets.UTF_8));
        }

        return new ModelAndView("redirect:" + url.toString());
    }

}
