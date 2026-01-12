package application.handler;

import application.model.Post;
import application.service.PostService;
import webserver.exception.BadRequestException;
import webserver.handler.DynamicHandler;
import webserver.http.HTTPRequest;
import webserver.http.HTTPResponse;
import webserver.session.SessionManager;

import java.util.Map;

public class PostCreateHandler extends DynamicHandler {
    private final PostService postService;
    private final SessionManager sessionManager;

    public PostCreateHandler(PostService postService, SessionManager sessionManager) {
        this.postService = postService;
        this.sessionManager = sessionManager;
    }

    @Override
    public HTTPResponse handle(HTTPRequest request) {
        String sid = request.getSid();
        String userId = sessionManager.getUser(sid).getUserId();

        Post post = createPost(request, userId);
        postService.create(post);
        return HTTPResponse.redirect("/index.html");
    }

    private Post createPost(HTTPRequest request, String userId) {
        Map<String, String> bodyParams = request.getBodyParams();
        if(!isValidCreateParams(bodyParams)) {
            throw BadRequestException.missingParameters();
        }

        Post post = new Post(bodyParams.get("content"), userId);
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
