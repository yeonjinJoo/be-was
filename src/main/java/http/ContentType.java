package http;

public enum ContentType {

    HTML("text/html;charset=utf-8", ".html", "/"),
    CSS("text/css", ".css"),
    JS("application/javascript", ".js"),
    ICO("image/x-icon", ".ico"),
    SVG("image/svg+xml", ".svg"),
    PNG("image/png", ".png"),
    JPG("image/jpeg", ".jpg", ".jpeg"),
    OCTET_STREAM("application/octet-stream");

    private final String contentType;
    private final String[] extensions;

    ContentType(String contentType, String... extensions) {
        this.contentType = contentType;
        this.extensions = extensions;
    }

    public String getContentType() {
        return contentType;
    }

    public boolean matches(String path) {
        for (String ext : extensions) {
            if (path.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    public static ContentType fromPath(String path) {
        for (ContentType type : values()) {
            if (type.matches(path)) {
                return type;
            }
        }
        return OCTET_STREAM;
    }
}
