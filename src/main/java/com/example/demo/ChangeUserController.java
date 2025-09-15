package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChangeUserController {

    @FXML
    private TextField adtText;

    @FXML
    private Button returnButton;

    private List<User> users;

    @FXML
    private void initialize() {
        // Load books from serialization file
        loadUsers();
    }

    @FXML
    private void returnButtonClicked(ActionEvent event) {
        // Implement the logic to return to the admin options screen
        try {
            // Load the adminOptions.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminOptions.fxml"));
            Parent root = loader.load();

            // Get the stage and set the new scene
            Stage stage = (Stage) returnButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void searchClicked(ActionEvent event) {
        String adt = adtText.getText();
        User foundUser = searchUserByAdt(Integer.parseInt(adt));

        if (foundUser != null) {
            try {
                // Load the BookInformation.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("userInformation.fxml"));
                Parent root = loader.load();

                // Get the controller and pass the found book and the list of books
                UserInformationController controller = loader.getController();
                controller.initData(foundUser, users);

                // Set the scene
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle exception: unable to load BookInformation.fxml
            }
        } else {
            // Book not found, show a message to the user
        }
    }

    private User searchUserByAdt(int adt) {
        for (User user : users) {
            if (user.getAdt() == adt) {
                return user;
            }
        }
        return null;
    }

    private void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("medialab/users.ser"))) {
            users = (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle exception: unable to load books
            users = new ArrayList<>(); // Initialize an empty list
        }
    }
}
