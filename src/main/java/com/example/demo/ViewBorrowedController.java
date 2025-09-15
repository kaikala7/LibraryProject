package com.example.demo;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.*;
import java.util.List;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
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
import java.util.List;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;


import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;


public class ViewBorrowedController {

    @FXML
    private ListView<Book> borrowedBooksList;

    @FXML
    private Button returnButton;

    @FXML
    private Button refreshButton;
    private List<Book> books = loadBooksFromSerializedFile();

    private List<User> users = loadUsersFromSerializedFile();

    private static final String BOOKS_SERIALIZED_FILE = "medialab/books.ser";

    private static final String USERS_SERIALIZED_FILE = "medialab/users.ser";
    private List<Book> borrowed = LoginController.currentUser.getBorrowedBooks();

    public void initialize() {

        books = loadBooksFromSerializedFile();
        users = loadUsersFromSerializedFile();
        borrowedBooksList.getItems().addAll(borrowed);
        borrowedBooksList.setCellFactory(new Callback<ListView<Book>, ListCell<Book>>() {
            @Override
            public ListCell<Book> call(ListView<Book> param) {
                return new ListCell<Book>() {
                    private final Button commentButton = new Button("Comment");
                    private final Button rateButton = new Button("Rate");

                    private final Button returnBookButton = new Button("Return");
                    private final HBox hbox = new HBox();

                    {
                        commentButton.setOnAction(event -> {
                            Book selectedBook = getItem();
                            openCommentPrompt(selectedBook);
                        });

                        rateButton.setOnAction(event -> {
                            Book selectedBook = getItem();
                            openRatingPrompt(selectedBook);
                        });

                        returnBookButton.setOnAction(event -> {
                            Book selectedBook = getItem();
                            openReturnPrompt(selectedBook);
                        });



                        hbox.getChildren().addAll(new Text(), commentButton, rateButton,returnBookButton);
                        HBox.setHgrow(new Text(), javafx.scene.layout.Priority.ALWAYS);
                    }

                    @Override
                    protected void updateItem(Book item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            Text titleText = new Text(item.getTitle());
                            hbox.getChildren().set(0, titleText);
                            setGraphic(hbox);
                        }
                    }
                };
            }
        });
    }

    @FXML
    private void handleReturnButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("plainOptions.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) returnButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openCommentPrompt(Book selectedBook) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Comment");
        dialog.setHeaderText("Enter your comment for " + selectedBook.getTitle());
        dialog.setContentText("Comment:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(comment -> {
            // Add the comment to the book's comments list
            for (Book book : books) {
                if (book.getTitle().equals(selectedBook.getTitle())) {
                    // Update the book with decreased number of copies
                    book.getComments().add(comment);
                    break; // No need to continue iterating once found
                }
            }
            saveBooksToSerializedFile(books);

            System.out.println("Comment added for " + selectedBook.getTitle() + ": " + comment);
        });
    }

    private void openRatingPrompt(Book selectedBook) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Rating");
        dialog.setHeaderText("Enter your rating (an integer between 1 and 5) for " + selectedBook.getTitle());
        dialog.setContentText("Rating:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(ratingStr -> {
            try {
                int rating = Integer.parseInt(ratingStr);
                if (rating >= 1 && rating <= 5) {
                    // Add the rating to the book's ratings list
                    for (Book book : books) {
                        if (book.getTitle().equals(selectedBook.getTitle())) {
                            // Update the book with the new rating
                            book.addRating(rating);
                            book.increaseNumRatings();
                            break; // No need to continue iterating once found
                        }
                    }
                    saveBooksToSerializedFile(books);
                    System.out.println("Rating added for " + selectedBook.getTitle() + ": " + rating);
                } else {
                    // Handle invalid rating
                    System.out.println("Invalid rating. Please enter an integer between 1 and 5.");
                }
            } catch (NumberFormatException e) {
                // Handle invalid input
                System.out.println("Invalid input. Please enter an integer between 1 and 5.");
            }
        });
    }

    private void openReturnPrompt(Book selectedBook) {

        for (User user :users) {
            if (user.getAdt() ==(LoginController.currentUser.getAdt())) {
                // Update the book with decreased number of copies
                //user.returnBook(selectedBook);
                // Before removal
                System.out.println("Before removal: " + user.getBorrowedBooks());

                boolean removed = user.getBorrowedBooks().remove(selectedBook);
                if(removed) System.out.println("Removed");
                else {
                    // Create an Alert
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Book Not Borrowed");
                    alert.setHeaderText(null);
                    alert.setContentText("You no longer have this book borrowed!");

                    // Show the Alert
                    alert.showAndWait();
                }
                break; // No need to continue iterating once found
            }
        }
        // Update the books list to reflect the changes
        for (Book book : books) {
            if (book.getIsbn().equals(selectedBook.getIsbn())) {
                // Update the book with decreased number of copies
                book.increaseNumCopies();
                break; // No need to continue iterating once found
            }
        }
        saveBooksToSerializedFile(books);
        saveUsersToSerializedFile(users);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Book Not Borrowed");
        alert.setHeaderText(null);
        alert.setContentText("Please refresh to see your updated borrowed books list.");

        // Show the Alert
        alert.showAndWait();
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
        return null; // Return null if there was an error or the file doesn't exist
    }

    public void saveUsersToSerializedFile(List<User> users) {
        for (User u : users) {
            System.out.println("User: " + u.getUsername());
            System.out.println("Borrowed Books:");
            for (Book book : u.getBorrowedBooks()) {
                System.out.println("- " + book.getTitle());
            }
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_SERIALIZED_FILE))) {
            oos.writeObject(users);
           // System.out.println("Users serialized successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static List<User> loadUsersFromSerializedFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_SERIALIZED_FILE))) {
            return (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null; // Return an empty list if there was an error or the file doesn't exist
    }



    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                LoginController.currentUser = user;
                return user;
            }
        }
        return null;
    }
    @FXML
    private void handleRefreshButton(ActionEvent event) {
        // Retrieve updated user information, for example, from a database or a service
        User updatedUser = getUserByUsername(LoginController.currentUser.getUsername()) ;// Implement this method according to your application's logic

        // Update LoginController.currentUser with the updated user
        LoginController.currentUser = updatedUser;
        borrowedBooksList.getItems().clear();
        borrowedBooksList.getItems().addAll(LoginController.currentUser.getBorrowedBooks());
    }


}
