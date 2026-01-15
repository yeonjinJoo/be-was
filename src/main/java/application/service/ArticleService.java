package application.service;

import application.model.Article;
import application.repository.ArticleRepository;

public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void create(Article article) {
        articleRepository.addArticle(article);
    }
}
