package application.handler;

import application.model.Article;
import application.service.ArticleService;
import webserver.exception.webexception.BadRequestException;
import webserver.handler.DynamicHandler;
import webserver.http.HTTPRequest;
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
        String sid = request.getSid();
        int authorId = sessionManager.getUser(sid).getId();

        Article article = createArticle(request, authorId);
        articleService.create(article);
        return new ModelAndView("redirect:/index.html");
    }

    private Article createArticle(HTTPRequest request, int authorId) {
        Map<String, String> bodyParams = request.getBodyParams();
        if(!isValidCreateParams(bodyParams)) {
            throw BadRequestException.missingParameters();
        }

        Article article = new Article(bodyParams.get("content"), authorId);
        return article;
    }

    private boolean isValidCreateParams(Map<String, String> qp) {
        if (qp == null || qp.isEmpty()) return false;

        return isPresent(qp, "content");
    }

    private boolean isPresent(Map<String, String> qp, String key) {
        String v = qp.get(key);
        return v != null && !v.isBlank();
    }
}
