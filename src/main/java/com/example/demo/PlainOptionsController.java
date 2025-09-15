package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
public class PlainOptionsController {

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button searchBooksButton;
    @FXML
    private Button viewAllBooksButton;
    @FXML
    private Button borrowBooksButton;

    @FXML
    public void initialize() {

    }

    @FXML
    private void handleSearchBooks() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("searchBook.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) searchBooksButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewAllBooks() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("viewAllBooks.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) viewAllBooksButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewBorrowed() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("viewBorrowed.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) borrowBooksButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
