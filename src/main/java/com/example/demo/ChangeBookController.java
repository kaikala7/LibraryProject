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

public class ChangeBookController {

    @FXML
    private TextField isbnField;

    @FXML
    private Button returnButton;

    private List<Book> books;

    @FXML
    private void initialize() {
        // Load books from serialization file
        loadBooks();
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
        String isbn = isbnField.getText();
        Book foundBook = searchBookByISBN(isbn);

        if (foundBook != null) {
            try {
                // Load the BookInformation.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("BookInformation.fxml"));
                Parent root = loader.load();

                // Get the controller and pass the found book and the list of books
                BookInformationController controller = loader.getController();
                controller.initData(foundBook, books);

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

    private Book searchBookByISBN(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }

    private void loadBooks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("medialab/books.ser"))) {
            books = (List<Book>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle exception: unable to load books
            books = new ArrayList<>(); // Initialize an empty list
        }
    }
}
