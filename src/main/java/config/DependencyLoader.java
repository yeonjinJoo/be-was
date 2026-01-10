package config;

import webserver.Dispatcher;

public class DependencyLoader {
    private final AppConfig appConfig;
    public final Dispatcher dispatcher;

    public DependencyLoader(){
        this.appConfig = new AppConfig();
        dispatcher = appConfig.router();
    }
}