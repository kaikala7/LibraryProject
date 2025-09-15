package com.example.demo;
import java.util.List;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.ListCell;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.ListView;
import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class LoginController {
    private List<User> users;

    private List<Book> books;


    private static final String BOOKS_SERIALIZED_FILE = "medialab/books.ser";
    private static final String USERS_SERIALIZED_FILE = "medialab/users.ser";


    public static User currentUser;
    @FXML
    private ImageView bookImageView;

    @FXML
    private ImageView brandingImageView;

    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField usernameText;

    @FXML
    private PasswordField enterPasswordField;


    @FXML
    private ListView<Book> topRatedBooksListView;
    @FXML
    private void initialize() {
        // Load user data from the serialized file
        loadUserData();

        // Set branding and book images
        loadBooksFromSerializedFile();

        // Clear existing items in the ListView
        topRatedBooksListView.getItems().clear();

        // Set a custom cell factory to display both title and rating
        topRatedBooksListView.setCellFactory(param -> new ListCell<Book>() {
            protected void updateItem(Book book, boolean empty) {
                super.updateItem(book, empty);

                if (empty || book == null) {
                    setText(null);
                } else {
                    // Set the text to display both title and rating
                    setText(book.getTitle() + " - " + book.getAuthor() + " - " + book.getIsbn() + " - " + book.getAverageRating() + " - " + book.getNumberOfRatings());
                }
            }
        });

        // Sort books based on average rating
        Collections.sort(books, Comparator.comparingDouble(Book::getAverageRating).reversed());

        // Add sorted books to the ListView
        if (books != null && !books.isEmpty()) {
            topRatedBooksListView.getItems().addAll(books);
        } else {
            System.out.println("No books found or error loading books.");
        }

        File bookFile = new File("images/otinanai.png");
        Image bookImage = new Image(bookFile.toURI().toString());
        bookImageView.setImage(bookImage);
    }


    private void loadUserData() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("medialab/users.ser"))) {
            Object obj = objectInputStream.readObject();
            if (obj instanceof List) {
                users = (List<User>) obj;
            } else {
                System.err.println("Error: The object read from the file is not a List<User>.");
                users = new ArrayList<>(); // Initialize an empty list
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading serialized user data: " + e.getMessage());
            users = new ArrayList<>(); // Handle the error appropriately, such as initializing an empty list
        }
    }



    @FXML
    private void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile = new File("images/otinanai2.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File bookFile = new File("images/otinanai.png");
        Image bookImage = new Image(bookFile.toURI().toString());
        bookImageView.setImage(bookImage);
    }

    private boolean isAdminFromSerializedData(String username) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("medialab/users.ser"))) {
            Object obj = objectInputStream.readObject();
            if (obj instanceof List) {
                List<User> users = (List<User>) obj;
                for (User user : users) {
                    if (user.getUsername().equals(username)) {
                        return user.isAdmin();
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading serialized user data: " + e.getMessage());
        }
        return false;
    }

    @FXML
    private void loginButtonAction(ActionEvent event) {
        String username = usernameText.getText();
        String password = enterPasswordField.getText();

        boolean loginSuccessful = validateLogin(username, password);

        if (loginSuccessful) {
            messageLabel.setText("Login successful");
            System.out.println("Login successful");
            currentUser = getUserByUsername(username);


            if (currentUser != null) {
                System.out.println("User attributes:");
                System.out.println("Username: " + currentUser.getUsername());
                System.out.println("Password: " + currentUser.getPassword());
                System.out.println("First Name: " + currentUser.getFirstName());
                System.out.println("Books Borrowed: " + currentUser.getBorrowedBooks());
                // Print other attributes as needed
            } else {
                System.out.println("User not found!");
            }

            if (isAdminFromSerializedData(username)) {
                navigateToAdminOptions();
            } else {
                navigateToPlainOptions();
            }
        } else {
            messageLabel.setText("Login failed");
            System.out.println("Login failed");
        }
    }
    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                currentUser = user;
                return user;
            }
        }
        return null;
    }


    private void navigateToPlainOptions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("plainOptions.fxml"));
            Parent plainOptionsParent = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(plainOptionsParent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateLogin(String username, String password) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("medialab/users.ser"))) {
            Object object = objectInputStream.readObject();
            if (object instanceof List) {
                List<User> users = (List<User>) object;
                for (User user : users) {
                    if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading serialized user data: " + e.getMessage());
        }
        return false;
    }


    @FXML
    private void signupButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) signupButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void navigateToAdminOptions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminOptions.fxml"));
            Parent adminParent = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(adminParent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void saveBooksToSerializedFile(List<Book> books) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKS_SERIALIZED_FILE))) {
            oos.writeObject(books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadBooksFromSerializedFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BOOKS_SERIALIZED_FILE))) {
            books = (List<Book>) ois.readObject();
            // Do something with the loaded books
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
