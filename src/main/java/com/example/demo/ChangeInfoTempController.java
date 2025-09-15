package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ChangeInfoTempController {

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button viewdeleteUsersButton;

    @FXML
    private Button changeBookButton;

    @FXML
    private Button returnButton;
    @FXML
    private Button changeUserButton;

    @FXML
    private Button viewBorrowingsButton;
    @FXML
    public void initialize() {

    }

    @FXML
    private void handleChangeBooks() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("changeBook.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) changeBookButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void returnButtonClicked(ActionEvent event) throws IOException {
        // Load the AdminOptions.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminOptions.fxml"));
        Parent root = loader.load();

        // Get the stage information
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleChangeUser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("changeUser.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) changeUserButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewDeleteUser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("viewDeleteUsers.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) viewdeleteUsersButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewBorrowings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("deleteBorrowing.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) changeUserButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
