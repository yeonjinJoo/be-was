package webserver.handler;

import webserver.http.ContentType;
import webserver.http.HTTPMethod;
import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;

import java.io.*;

public class StaticFileHandler implements Handler {
    private static final String baseResourcePath = "./src/main/resources/static";

    public boolean canHandle(HTTPMethod method, String path) {
        if (method == HTTPMethod.GET) {
            return true;
        }
        else return false;
    }

    public HTTPResponse handle(HTTPRequest request) {
        byte[] body;
        String path = request.getPath();

        try { // 200 - 정상 처리
            String resolvedPath = resolvePath(path);
            body = readFile(resolvedPath);
            String contentType = ContentType.fromPath(resolvedPath).getContentType();
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
}
