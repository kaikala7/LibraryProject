package com.example.demo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SignupController {

    private static final String FILENAME = "medialab/users.ser";

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField adtField;

    @FXML
    private TextField emailField;

    @FXML
    private Button signupButton;

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        // Initialization code, if needed
    }

    @FXML
    public void signupButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String name = nameField.getText();
        String surname = surnameField.getText();
        int adt = Integer.parseInt(adtField.getText());
        String email = emailField.getText();

        // Create a new user object
        User newUser = new User(username, password, name, surname, adt, email, false);

        // Deserialize existing user data
        List<User> userList = loadUserData();

        // Add the new user to the list
        userList.add(newUser);

        // Serialize the updated list of users
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            outputStream.writeObject(userList);
            System.out.println("User information successfully written to file.");
        } catch (IOException e) {
            System.err.println("Error writing user information to file: " + e.getMessage());
        }

        // Navigate to the login screen
        navigateToLoginScreen(event);
    }

    private void navigateToLoginScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent loginParent = loader.load();
            Scene loginScene = new Scene(loginParent);

            // Access the Stage from the ActionEvent
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<User> loadUserData() {
        List<User> userList = new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(FILENAME))) {
            Object obj = inputStream.readObject();
            if (obj instanceof List) {
                userList = (List<User>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading user information from file: " + e.getMessage());
        }
        return userList;
    }
}
