package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminOptionsController {
    @FXML
    private Button viewBooksButton;

    @FXML
    private Button deleteBooksButton;

    @FXML
    private Button addBooksButton;

    @FXML
    private Button addCategoryButton;

    @FXML
    private Button viewDeleteCategoryButton;

    @FXML
    private Button changeButton;

    @FXML
    void handleViewBooksButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("seeBooks.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) viewBooksButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void handleAddCategoryButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addCategory.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addCategoryButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleViewDeleteCategoryButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("viewDeleteCategories.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) viewDeleteCategoryButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleAddBooksButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addBook.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addBooksButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleDeleteBooksButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("deleteBook.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addBooksButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void handleChangeButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("changeInfoTemp.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addBooksButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
