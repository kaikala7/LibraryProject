package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class DeleteBorrowingController {
    @FXML
    private TableView<BorrowedBook> borrowingTableView;
    @FXML
    private TableColumn<User, Void> cancelButtonColumn;

    @FXML
    private Button returnButton;
    @FXML
    private TableColumn<BorrowedBook, String> userNameColumn;

    @FXML
    private TableColumn<BorrowedBook, String> bookNameColumn;



    private List<Book> books = loadBooksFromSerializedFile();
    private List<User> users = loadUsersFromSerializedFile();

    private static final String BOOKS_SERIALIZED_FILE = "medialab/books.ser";
    private static final String USERS_SERIALIZED_FILE = "medialab/users.ser";



    @FXML
    private void initialize() {
        // Load users from the serialized file
        List<User> users = loadUsersFromSerializedFile();

        // Set up columns
        userNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUser().getUsername()));
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));

        if (users != null) {
            for (User user : users) {
                List<Book> borrowedBooks = user.getBorrowedBooks();
                for (Book book : borrowedBooks) {
                    borrowingTableView.getItems().add(new BorrowedBook(user, book.getTitle()));
                }
            }
        }

        cancelButtonColumn.setCellFactory(param -> new TableCell<User, Void>() {
            private final Button cancelButton = new Button("Cancel");

            {
                cancelButton.setOnAction(event -> {
                    Button cancelButton = (Button) event.getSource();
                    TableCell<?, ?> cell = (TableCell<?, ?>) cancelButton.getParent();
                    TableRow<?> row = cell.getTableRow();
                    int rowIndex = row.getIndex();

                    // Retrieve the BorrowedBook associated with the clicked row
                    BorrowedBook borrowedBook = borrowingTableView.getItems().get(rowIndex);

                    // Call the cancelButtonAction method with the event and BorrowedBook
                    cancelButtonAction(event, borrowedBook);
                    saveUsersToSerializedFile(users);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(cancelButton);
                }
            }
        });
    }
    private void saveBooksToSerializedFile(List<Book> books) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKS_SERIALIZED_FILE))) {
            oos.writeObject(books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Book> loadBooksFromSerializedFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BOOKS_SERIALIZED_FILE))) {
            return (List<Book>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(); // Return an empty list if there was an error or the file doesn't exist
    }

    private void saveUsersToSerializedFile(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_SERIALIZED_FILE))) {
            oos.writeObject(users);
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
        return new ArrayList<>(); // Return an empty list if there was an error or the file doesn't exist
    }

    @FXML
    private void cancelButtonAction(ActionEvent event, BorrowedBook borrowedBook) {
        // Retrieve the user associated with the borrowed book
        User user = borrowedBook.getUser();

        // Find the corresponding book in the user's borrowed books list
        Book selectedBook = null;
        for (Book book : user.getBorrowedBooks()) {
            if (book.getTitle().equals(borrowedBook.getBookTitle())) {
                selectedBook = book;
                break;
            }
        }

        if (selectedBook != null) {
            // Remove the book from the user's borrowed books list
            boolean removed = user.getBorrowedBooks().remove(selectedBook);
            if (removed) {
                // Update the list of users in the serialized file
                saveUsersToSerializedFile(users);

                // Increment the number of copies of the book
                for (Book book : books) {
                    if (book.getIsbn().equals(selectedBook.getIsbn())) {
                        book.increaseNumCopies();
                        saveBooksToSerializedFile(books);
                        break;
                    }
                }

                // Display a success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Book Returned");
                alert.setHeaderText(null);
                alert.setContentText("The book has been returned successfully.");
                alert.showAndWait();

                // Remove the item from the table view
                borrowingTableView.getItems().remove(borrowedBook);
            } else {
                // Display an error message if the book was not found
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to return the book.");
                alert.showAndWait();
            }
        } else {
            // Display an error message if the book was not found
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to find the book in the user's borrowed books list.");
            alert.showAndWait();
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

    }
