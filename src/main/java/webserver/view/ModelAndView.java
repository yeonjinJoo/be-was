package webserver.view;

import webserver.http.HTTPResponse;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    private final String viewName;
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, Object> model = new HashMap<>();

    public ModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public void addObject(String key, Object value) {
        model.put(key, value);
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getViewName() { return viewName; }
    public Map<String, Object> getModel() { return model; }
}