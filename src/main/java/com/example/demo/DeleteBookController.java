package com.example.demo;
import java.util.List;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DeleteBookController {
    private static final String BOOKS_SERIALIZED_FILE = "medialab/books.ser";
    private static final String USERS_SERIALIZED_FILE = "medialab/users.ser";

    @FXML
    private TextField isbnField;

    @FXML
    private Button returnButton;

    @FXML
    private Button deleteButton;

    @FXML
    private void deleteButtonClicked(ActionEvent event) {
        // Get the ISBN from the TextField
        String isbn = isbnField.getText();

        // Call a method to delete the book from the serialized file
        boolean deleted = deleteBookFromSerializedFile(isbn);

        // Check if the book was successfully deleted
        if (deleted) {
            System.out.println("Book with ISBN " + isbn + " deleted successfully.");
            // You can show a message to the user indicating the book was deleted
        } else {
            System.out.println("Book with ISBN " + isbn + " not found or could not be deleted.");
            // You can show a message to the user indicating the book was not found or couldn't be deleted
        }
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

    private boolean deleteBookFromSerializedFile(String isbn) {
        // Load the list of books from the serialized file
        List<Book> books = loadBooksFromSerializedFile();

        if (books != null) {
            // Iterate through the list of books and remove the book with the given ISBN
            for (Book book : books) {
                if (book.getIsbn().equals(isbn)) {
                    books.remove(book);
                    saveBooksToSerializedFile(books); // Save the updated list back to the file

                    // Remove the book from the list of borrowed books of every user
                    deleteBookFromUserBorrowedBooks(book);

                    return true; // Return true if the book was successfully deleted
                }
            }
        }
        return false; // Return false if the book was not found or could not be deleted
    }

    private void deleteBookFromUserBorrowedBooks(Book book) {
        List<User> users = loadUsersFromSerializedFile();

        if (users != null) {
            for (User user : users) {
                user.returnBook(book);
            }
            saveUsersToSerializedFile(users);
        }
    }

    private List<Book> loadBooksFromSerializedFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BOOKS_SERIALIZED_FILE))) {
            return (List<Book>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null; // Return null if there was an error or the file doesn't exist
    }

    private void saveBooksToSerializedFile(List<Book> books) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKS_SERIALIZED_FILE))) {
            oos.writeObject(books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<User> loadUsersFromSerializedFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_SERIALIZED_FILE))) {
            return (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null; // Return null if there was an error or the file doesn't exist
    }

    private void saveUsersToSerializedFile(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_SERIALIZED_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
