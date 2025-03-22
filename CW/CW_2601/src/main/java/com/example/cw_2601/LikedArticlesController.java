package com.example.cw_2601;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;

public class LikedArticlesController {

    @FXML
    private ListView<String> likedArticlesListView;

    // Initializes the list of liked articles with a List<String>
    public void setLikedArticles(List<String> articles) {
        likedArticlesListView.getItems().addAll(articles);
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) likedArticlesListView.getScene().getWindow();
        stage.close();
    }
}
