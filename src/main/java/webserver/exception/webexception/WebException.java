package webserver.exception.webexception;

import webserver.http.HTTPStatus;

import java.util.HashMap;
import java.util.Map;

public abstract class WebException extends RuntimeException{
    private final HTTPStatus status;
    private final String redirectPath;
    private final Map<String, Object> modelAttributes = new HashMap<>();

    protected WebException(HTTPStatus status, String message, String redirectPath) {
        super(message);
        this.status = status;
        this.redirectPath = redirectPath;
    }

    public HTTPStatus getStatus() { return status; }

    public String getRedirectPath(){
        return redirectPath;
    }

    public WebException addAttribute(String key, Object value) {
        this.modelAttributes.put(key, value);
        return this;
    }

    public void fillModel(Map<String, Object> model){
        model.put("hasError", true);
        model.put("errorMessage", getMessage());
        model.putAll(modelAttributes);
    }
}
