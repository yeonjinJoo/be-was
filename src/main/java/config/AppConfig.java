package config;

import application.handler.PostCreateHandler;
import application.repository.PostRepository;
import application.repository.UserRepository;
import application.handler.UserCreateHandler;
import application.handler.UserLoginHandler;
import application.handler.UserLogoutHandler;
import application.service.PostService;
import application.service.UserService;
import webserver.HandlerMapping;
import webserver.Dispatcher;
import webserver.handler.StaticFileHandler;
import webserver.http.HTTPMethod;
import webserver.session.SessionManager;
import webserver.interceptor.InterceptorRegistry;
import webserver.interceptor.LoginCheckInterceptor;

public class AppConfig {

    private final UserRepository userRepository = new UserRepository();
    private final PostRepository postRepository = new PostRepository();

    private final UserService userService = new UserService(userRepository);
    private final PostService postService = new PostService(postRepository);

    private final SessionManager sessionManager = new SessionManager();

    private final StaticFileHandler staticFileHandler = new StaticFileHandler(sessionManager);
    private final UserCreateHandler userCreateHandler = new UserCreateHandler(userService);
    private final UserLoginHandler userLoginHandler = new UserLoginHandler(userService, sessionManager);
    private final UserLogoutHandler userLogoutHandler = new UserLogoutHandler(sessionManager);
    private final PostCreateHandler postCreateHandler = new PostCreateHandler(postService, sessionManager);

    private final LoginCheckInterceptor loginCheckInterceptor = new LoginCheckInterceptor(sessionManager);

    private final HandlerMapping handlerMapping = new HandlerMapping(staticFileHandler);

    private final InterceptorRegistry interceptorRegistry;
    private final Dispatcher dispatcher;

    public AppConfig() {
        this.interceptorRegistry = configureInterceptors();
        registerHandler();

        this.dispatcher = new Dispatcher(handlerMapping, interceptorRegistry);
    }

    public Dispatcher getDispatcher() {
        return this.dispatcher;
    }

    private InterceptorRegistry configureInterceptors() {
        InterceptorRegistry registry = new InterceptorRegistry();

        // 로그인 체크 인터셉터 등록 및 경로 설정
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/mypage/**", "/user/logout", "/article/**", "/main"); // 로그인이 필요한 페이지들

        return registry;
    }

    // Handlers
    private void registerHandler() {
        handlerMapping.register(HTTPMethod.POST, "/user/create", userCreateHandler);
        handlerMapping.register(HTTPMethod.POST, "/user/login", userLoginHandler);
        handlerMapping.register(HTTPMethod.POST, "/user/logout", userLogoutHandler);
        handlerMapping.register(HTTPMethod.POST, "/article/create", postCreateHandler);
    }
}
