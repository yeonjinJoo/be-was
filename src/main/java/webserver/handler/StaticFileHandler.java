//package webserver.handler;
//
//import application.model.User;
//import webserver.view.TemplateEngine;
//import webserver.exception.InternalServerException;
//import webserver.exception.NotFoundException;
//import webserver.http.ContentType;
//import webserver.http.HTTPRequest;
//import webserver.http.HTTPResponse;
//import webserver.session.SessionManager;
//
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.HashMap;
//import java.util.Map;
//
//public class StaticFileHandler implements Handler {
//    private static final String STATIC_PATH = "./src/main/resources/static";
//    private static final String TEMPLATE_PATH = "./src/main/resources/template";
//
//    private final SessionManager sessionManager;
//
//    public StaticFileHandler(SessionManager sessionManager){
//        this.sessionManager = sessionManager;
//    }
//
//    public HTTPResponse handle(HTTPRequest request) {
//        byte[] body;
//        String path = request.getPath();
//
//        // 1. template 먼저 실행
//        String templatePath = resolvePath(TEMPLATE_PATH, path);
//        File templateFile = new File(templatePath);
//
//        if(templateFile.exists() && templateFile.isFile()){
//            body = readFile(templatePath);
//            return HTTPResponse.ok("text/html", applyTemplate(request, body));
//        }
//
//        // 2. template에서 없다면 static 실행
//        String staticPath = resolvePath(STATIC_PATH, path);
//        File staticFile = new File(staticPath);
//
//        if(staticFile.exists() && staticFile.isFile()){
//            body = readFile(staticPath);
//            String contentType = ContentType.fromPath(staticPath).getContentType();
//            return HTTPResponse.ok(contentType, body);
//        }
//
//        throw NotFoundException.pageNotFound(path);
//    }
//
//    private byte[] readFile(String path) {
//        try (InputStream is = new FileInputStream(path)) {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//
//            int read;
//            while ((read = is.read(buffer)) != -1) {
//                baos.write(buffer, 0, read);
//            }
//
//            return baos.toByteArray();
//        } catch (IOException e){
//            throw InternalServerException.fileError();
//        }
//    }
//
//    private String resolvePath(String basePath, String path) {
//        String resourcePath = basePath + path;
//        File file = new File(resourcePath);
//
//        if (file.isDirectory()) {
//            resourcePath += path.endsWith("/") ? "index.html" : "/index.html";
//        }
//        return resourcePath;
//    }
//
//    private byte[] applyTemplate(HTTPRequest request, byte[] body) {
//        String html = new String(body, StandardCharsets.UTF_8);
//        User user = sessionManager.getUser(request.getSid());
//
//        String fragment = readFragment("nav_user.html").replace("{{userName}}", user.getName());
//
//        Map<String, String> data = new HashMap<>();
//        data.put("userStatus", fragment);
//
//        String renderedHtml = TemplateEngine.render(html, data);
//        return renderedHtml.getBytes(StandardCharsets.UTF_8);
//    }
//
//    private String readFragment(String fileName) {
//        String fragmentPath = TEMPLATE_PATH + "/fragment/" + fileName;
//        try {
//            return Files.readString(Paths.get(fragmentPath), StandardCharsets.UTF_8);
//        } catch (IOException e) {
//            return "";
//        }
//    }
//}
