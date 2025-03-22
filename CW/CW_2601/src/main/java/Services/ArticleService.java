package Services;

import Models.Article;
import com.example.cw_2601.DatabaseConnection;

import java.util.List;

public class ArticleService {
    private final DatabaseConnection dbConnection;

    public ArticleService(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<Article> fetchAllArticles() {
        return Article.fetchAllArticles(dbConnection);
    }

    public boolean isArticleLiked(int userId, int articleId) {
        return Article.isArticleLiked(dbConnection, userId, articleId);
    }

    public List<String> getLikedArticleTitles(int userId) {
        return Article.getLikedArticleTitles(dbConnection, userId);
    }

    public List<String> getSkippedArticleTitles(int userId) {
        return Article.getSkippedArticleTitles(dbConnection, userId);
    }
}
