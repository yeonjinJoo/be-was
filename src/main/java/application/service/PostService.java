package application.service;

import application.model.Post;
import application.repository.PostRepository;

public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void create(Post post) {
        postRepository.addPost(post);
    }
}
