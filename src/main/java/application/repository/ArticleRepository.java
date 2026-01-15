package application.repository;

import application.model.Article;

import java.sql.Connection;

public interface ArticleRepository {
    public void addArticle(Connection conn, Article article);
}
