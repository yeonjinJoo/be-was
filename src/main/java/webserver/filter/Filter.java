package webserver.filter;

import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;

import java.util.Optional;

public interface Filter {
    public Optional<HTTPResponse> doFilter(HTTPRequest request);
}
