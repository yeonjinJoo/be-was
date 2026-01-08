package webserver;

import webserver.http.HTTPMethod;

public record RouteKey(HTTPMethod method, String path) {
}
