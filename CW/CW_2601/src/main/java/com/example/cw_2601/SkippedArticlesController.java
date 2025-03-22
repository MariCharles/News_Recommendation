package com.example.cw_2601;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;

public class SkippedArticlesController {

    @FXML
    private ListView<String> skippedArticlesListView;

    public void setSkippedArticles(List<String> articles) {
        skippedArticlesListView.getItems().clear();
        skippedArticlesListView.getItems().addAll(articles);
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) skippedArticlesListView.getScene().getWindow();
        stage.close();
    }
}
