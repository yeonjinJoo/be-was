package application.service;

import application.ImageStorage;
import application.model.Article;
import application.repository.ArticleRepository;
import application.repository.JdbcTx;
import webserver.multipart.UploadedFile;

public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ImageStorage imageStorage;

    public ArticleService(ArticleRepository articleRepository, ImageStorage imageStorage) {
        this.articleRepository = articleRepository;
        this.imageStorage = imageStorage;
    }

    public void create(int authorId, String content, UploadedFile image) {
        String savedName = null;
        try {
            // 파일 저장
            savedName = imageStorage.save(image);
            String imageUrl = "/img/uploads/" + savedName;

            Article article = new Article(content, authorId, imageUrl);

            // DB 저장
            JdbcTx.execute(conn -> {
                articleRepository.addArticle(conn, article);
                return null;
            });

        } catch (RuntimeException e) {
            // DB 저장 실패 시 파일 삭제
            if (savedName != null) {
                imageStorage.deleteQuietly(savedName);
            }
            throw e;
        }
    }
}
