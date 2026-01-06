package config;

import webserver.Router;

public class DependencyLoader {
    private final AppConfig appConfig;
    public final Router router;

    public DependencyLoader(){
        this.appConfig = new AppConfig();
        router = appConfig.router();
    }
}