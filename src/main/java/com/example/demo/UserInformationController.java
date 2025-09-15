package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class UserInformationController {

    private User user;
    private List<User> users;

    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label adtLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField adt;

    @FXML
    private TextField email;


    @FXML
    private Button saveButton;

    public void initData(User user, List<User> users) {
        this.user = user;
        this.users = users;
        username.setText(user.getUsername());
        password.setText(user.getPassword());
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        email.setText(String.valueOf(user.getEmail()));
        adt.setText(String.valueOf(user.getAdt()));
        usernameLabel.setText("Username:");
        passwordLabel.setText("Password:");
        firstNameLabel.setText("First Name:");
        lastNameLabel.setText("Last Name:");
        emailLabel.setText("Email:");
        adtLabel.setText("ADT:");
    }

    @FXML
    private void saveChangesClicked() {
        String newUsername = username.getText();
        String newPassword = password.getText();
        String newFirstName = username.getText();
        String newLastName = lastName.getText();
        String newAdt = adt.getText();
        String newEmail = email.getText();

        user.setUsername(newUsername);
        user.setPassword(newPassword);
        user.setFirstName(newFirstName);
        user.setLastName(newLastName);
        user.setEmail(newEmail);


        updateUserInList(user);

        serializeUsers();

        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();

        // Load adminOptions.fxml and show it
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminOptions.fxml"));
            Parent root = loader.load();
            Stage adminStage = new Stage();
            adminStage.setScene(new Scene(root));
            adminStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateUserInList(User updatedUser) {
        for (User b : users) {
            if (b.getAdt() == (updatedUser.getAdt())) {
                b.setUsername(updatedUser.getUsername());
                b.setPassword(updatedUser.getPassword());
                b.setFirstName(updatedUser.getFirstName());
                b.setLastName(updatedUser.getLastName());
                b.setEmail(updatedUser.getEmail());
                break;
            }
        }
    }

    private void serializeUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("medialab/users.ser"))) {
            oos.writeObject(users);
            System.out.println("Books serialized successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
