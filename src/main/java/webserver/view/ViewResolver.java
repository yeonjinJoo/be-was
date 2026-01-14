package webserver.view;

import webserver.exception.NotFoundException;
import webserver.http.ContentType;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ViewResolver {
    private static final String TEMPLATE_PATH = "./src/main/resources/template";
    private static final String STATIC_PATH = "./src/main/resources/static";

    public static View resolve(String viewName) throws Exception {
        // 1. Redirect
        if (viewName.startsWith("redirect:")) {
            return new RedirectView(viewName.substring(9));
        }

        // 2. Template
        if(viewName.startsWith("template:")){
            String templatePath = resolvePath(TEMPLATE_PATH, viewName.substring(9));
            if (Files.exists(Paths.get(templatePath))) {
                return new TemplateView(templatePath);
            }
        }

        // 3. Static file
        String staticPath = resolvePath(STATIC_PATH, viewName);

        if (Files.exists(Paths.get(staticPath))) {
            String contentType = ContentType.fromPath(staticPath).getContentType();
            return new StaticView(Files.readAllBytes(Paths.get(staticPath)), contentType);
        }

        throw NotFoundException.pageNotFound(viewName);
    }

    private static String resolvePath(String basePath, String path) {
        String resolvedPath = basePath + (path.startsWith("/") ? "" : "/") + path;
        File file = new File(resolvedPath);

        if (file.isDirectory()) {
            resolvedPath += path.endsWith("/") ? "index.html" : "/index.html";
        }
        return resolvedPath;
    }
}