package application.handler;

import application.model.Article;
import application.model.User;
import application.service.ArticleService;
import webserver.exception.webexception.BadRequestException;
import webserver.handler.DynamicHandler;
import webserver.http.HTTPRequest;
import webserver.multipart.MultipartParser;
import webserver.multipart.UploadedFile;
import webserver.session.SessionManager;
import webserver.view.ModelAndView;

import java.util.Map;

public class ArticleCreateHandler extends DynamicHandler {
    private final ArticleService articleService;
    private final SessionManager sessionManager;

    public ArticleCreateHandler(ArticleService articleService, SessionManager sessionManager) {
        this.articleService = articleService;
        this.sessionManager = sessionManager;
    }

    @Override
    public ModelAndView handle(HTTPRequest request) {
        MultipartParser.applyTo(request);
        User user = sessionManager.getUser(request.getSid());

        int authorId = user.getId();
        String content = request.getBodyParams().getOrDefault("content", "");
        UploadedFile image = request.getFirstFile("image");
        if (image == null) throw BadRequestException.missingArticleImage();

        articleService.create(authorId, content, image);

        ModelAndView modelAndView = new ModelAndView("redirect:/");
        modelAndView.addObject("userName", user.getName());
        modelAndView.addObject("userStatus", "nav_user.html");
        return modelAndView;
    }
}
