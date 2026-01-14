package config;

import application.handler.*;
import application.repository.H2PostRepository;
import application.repository.H2UserRepository;
import application.repository.PostRepository;
import application.repository.UserRepository;
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

    private final UserRepository userRepository = new H2UserRepository();
    private final PostRepository postRepository = new H2PostRepository();

    private final UserService userService = new UserService(userRepository);
    private final PostService postService = new PostService(postRepository);

    private final SessionManager sessionManager = new SessionManager();

    // TODO: 로그인 상태면 handler에게 handle 넘길 때 sessionManager가 받아온 User 함께 넘기게 하기
    private final StaticFileHandler staticFileHandler = new StaticFileHandler();
    private final UserCreateHandler userCreateHandler = new UserCreateHandler(userService);
    private final UserLoginHandler userLoginHandler = new UserLoginHandler(userService, sessionManager);
    private final UserLogoutHandler userLogoutHandler = new UserLogoutHandler(sessionManager);
    private final PostCreateHandler postCreateHandler = new PostCreateHandler(postService, sessionManager);
    private final MainPageHandler mainPageHandler = new MainPageHandler(sessionManager);
    private final MyPageHandler myPageHandler = new MyPageHandler(sessionManager);

    private final LoginCheckInterceptor loginCheckInterceptor = new LoginCheckInterceptor(sessionManager);

    private final HandlerMapping handlerMapping = new HandlerMapping(staticFileHandler);

    private final InterceptorRegistry interceptorRegistry;
    private final Dispatcher dispatcher;

    public AppConfig() {
        this.interceptorRegistry = configureInterceptors();
        registerHandler();

        this.dispatcher = new Dispatcher(handlerMapping, interceptorRegistry);

        DBConfig.init();
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
        // TODO: MainPageHandler 추가
        handlerMapping.register(HTTPMethod.POST, "/user/create", userCreateHandler);
        handlerMapping.register(HTTPMethod.POST, "/user/login", userLoginHandler);
        handlerMapping.register(HTTPMethod.POST, "/user/logout", userLogoutHandler);
        handlerMapping.register(HTTPMethod.POST, "/article/create", postCreateHandler);
        handlerMapping.register(HTTPMethod.GET, "/mypage", myPageHandler);
        handlerMapping.register(HTTPMethod.GET, "/", mainPageHandler);
        handlerMapping.register(HTTPMethod.GET, "/index.html", mainPageHandler);
    }
}
