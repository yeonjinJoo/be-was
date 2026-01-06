package webserver.handler;

import http.HTTPMethod;
import http.HTTPRequest;
import http.HTTPResponse;

import java.io.*;

public class StaticFileHandler implements Handler {
    private static final String baseResourcePath = "./src/main/resources/static";

    public boolean canHandle(HTTPMethod method, String path) {
        if (method != HTTPMethod.GET) {
            return false;
        }
        else return true;
    }

    public boolean canHandleMethod(HTTPMethod method) {
        return method == HTTPMethod.GET;
    }

    public HTTPResponse handle(HTTPRequest request) {
        byte[] body;
        String path = request.getPath();

        try { // 200 - 정상 처리
            String resolvedPath = resolvePath(path);
            body = readFile(resolvedPath);
            String contentType = getContentType(resolvedPath);
            return HTTPResponse.ok(contentType, body);
        } catch (FileNotFoundException e) { // 404 - 파일 존재 x
            return HTTPResponse.notFound();
        } catch (IOException e) { // 500 - 서버 오류
            return HTTPResponse.internalServerError();
        }
    }

    private byte[] readFile(String path) throws IOException {
        File file = new File(path);

        //위의 path에 파일이 존재하지 않는 경우
        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException("Resource not found: " + path);
        }

        try (InputStream is = new FileInputStream(path)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];

            int read;
            while ((read = is.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }

            return baos.toByteArray();
        }
    }

    private String resolvePath(String path) {
        String resourcePath = baseResourcePath + path;
        File file = new File(resourcePath);

        if (file.isDirectory()) {
            resourcePath += path.endsWith("/") ? "index.html" : "/index.html";
        }
        return resourcePath;
    }

    private String getContentType(String path) {
        if (path.endsWith(".html") || path.endsWith("/")) return "text/html;charset=utf-8";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".ico")) return "image/x-icon";
        if (path.endsWith(".svg")) return "image/svg+xml";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        return "application/octet-stream"; // 알 수 없는 확장자로, 다운로드 처리
    }
}
