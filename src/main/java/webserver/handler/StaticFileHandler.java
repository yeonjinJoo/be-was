package webserver.handler;

import webserver.exception.InternalServerException;
import webserver.exception.NotFoundException;
import webserver.http.ContentType;
import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;

import java.io.*;

public class StaticFileHandler implements Handler {
    private static final String baseResourcePath = "./src/main/resources/static";

    public HTTPResponse handle(HTTPRequest request) {
        byte[] body;
        String path = request.getPath();

        String resolvedPath = resolvePath(path);
        body = readFile(resolvedPath);
        String contentType = ContentType.fromPath(resolvedPath).getContentType();

        return HTTPResponse.ok(contentType, body);
    }

    private byte[] readFile(String path) {
        File file = new File(path);

        if (!file.exists() || !file.isFile()) {
            throw NotFoundException.pageNotFound(path);
        }

        try (InputStream is = new FileInputStream(path)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];

            int read;
            while ((read = is.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }

            return baos.toByteArray();
        } catch (IOException e){
            throw InternalServerException.fileError();
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
