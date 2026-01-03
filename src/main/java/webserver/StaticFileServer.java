package webserver;

import webserver.http.HTTPResponse;

import java.io.*;

public class StaticFileServer {
    private static final String baseResourcePath = "./src/main/resources/static";

    public static HTTPResponse serve(String path) {
        byte[] body;
        String contentType = getContentType(path);
        try { // 200 - 정상 처리
            body = readFile(path);
            return HTTPResponse.ok(contentType, body);
        } catch (FileNotFoundException e) { // 404 - 파일 존재 x
            return HTTPResponse.notFound();
        } catch (IOException e) { // 500 - 서버 오류
            return HTTPResponse.internalServerError();
        }
    }

    private static byte[] readFile(String path) throws IOException {
        File file = new File(baseResourcePath + path);

        // 파일이 존재하지 않는 경우
        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException(file.getPath());
        }

        String resourcePath = baseResourcePath + path;

        try (InputStream is = new FileInputStream(resourcePath)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];

            int read;
            while ((read = is.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }

            return baos.toByteArray();
        }
    }

    private static String getContentType(String path) {
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
