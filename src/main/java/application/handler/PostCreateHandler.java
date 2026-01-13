package application.handler;

import application.model.Post;
import application.service.PostService;
import webserver.exception.BadRequestException;
import webserver.handler.DynamicHandler;
import webserver.http.HTTPRequest;
import webserver.session.SessionManager;
import webserver.view.ModelAndView;

import java.util.Map;

public class PostCreateHandler extends DynamicHandler {
    private final PostService postService;
    private final SessionManager sessionManager;

    public PostCreateHandler(PostService postService, SessionManager sessionManager) {
        this.postService = postService;
        this.sessionManager = sessionManager;
    }

    @Override
    public ModelAndView handle(HTTPRequest request) {
        String sid = request.getSid();
        int authorId = sessionManager.getUser(sid).getId();

        Post post = createPost(request, authorId);
        postService.create(post);
        return new ModelAndView("redirect:/index.html");
    }

    private Post createPost(HTTPRequest request, int authorId) {
        Map<String, String> bodyParams = request.getBodyParams();
        if(!isValidCreateParams(bodyParams)) {
            throw BadRequestException.missingParameters();
        }

        Post post = new Post(bodyParams.get("content"), authorId);
        return post;
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
