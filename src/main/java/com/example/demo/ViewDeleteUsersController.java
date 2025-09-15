package com.example.demo;
import javafx.scene.control.TableCell;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;

public class ViewDeleteUsersController {




@FXML
private TableView<User> tableView;

@FXML
private TableColumn<User, String> usernameColumn;

@FXML
private TableColumn<User, String> firstNameColumn;

@FXML
private TableColumn<User, String> lastNameColumn;

@FXML
private TableColumn<User, String> adtColumn ;

@FXML
private TableColumn<User, String> emailColumn;


@FXML
private Button returnButton;

private List<User> users;

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
    public void initialize() {
        // Initialize columns
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        adtColumn.setCellValueFactory(new PropertyValueFactory<>("adt"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Create the delete button column
        TableColumn<User, Void> deleteButtonColumn = new TableColumn<>("Delete");
        deleteButtonColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex()); // Get the user associated with this row
                    int adt = user.getAdt(); // Get the adt of the user

                    if (deleteUserFromSerializedFile(adt)) {
                        // If user deletion is successful, remove the user from the TableView
                        getTableView().getItems().remove(user);
                    } else {
                        System.out.println("Failed to delete user with ADT: " + adt);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        // Add the delete button column to the table view
        tableView.getColumns().add(deleteButtonColumn);

        // Load data from serialized file
        loadUsersFromSerializedFile();

        // Add loaded books to the table view
        tableView.getItems().addAll(users);
    }

private void loadUsersFromSerializedFile() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("medialab/users.ser"))) {
        users = (List<User>) ois.readObject();
        System.out.println("Books loaded successfully.");
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace(); // Handle error loading books from serialized file
    }
}

    private void saveUsersToSerializedFile(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("medialab/users.ser"))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean deleteUserFromSerializedFile(int adt) {
        // Load the list of users from the serialized file
        loadUsersFromSerializedFile();

        if (users != null) {
            // Iterate through the list of users and remove the user with the given ADT
            for (User user : users) {
                if (user.getAdt() == adt) {
                    List<Book> borrowedBooks = user.getBorrowedBooks();
                    // Increase the numCopies of each borrowed book by 1
                    for (Book book : borrowedBooks) {
                        book.setNumCopies(book.getNumCopies() + 1);
                    }
                    // Save the updated list of books
                    saveBooksToSerializedFile(user.getBorrowedBooks());

                    // Remove the user from the list of users
                    users.remove(user);

                    // Save the updated list of users back to the file
                    saveUsersToSerializedFile(users);

                    // Reload all books from the file
                    List<Book> allBooks = loadBooksFromSerializedFile();
                    // Update the copies of borrowed books in the allBooks list
                    for (Book borrowedBook : borrowedBooks) {
                        for (Book book : allBooks) {
                            if (book.getIsbn() == borrowedBook.getIsbn()) {
                                book.setNumCopies(borrowedBook.getNumCopies() );
                                break; // No need to continue searching
                            }
                        }
                    }
                    // Save the updated list of all books back to the file
                    saveBooksToSerializedFile(allBooks);

                    return true; // Return true if the user was successfully deleted
                }
            }
        }
        return false; // Return false if the user was not found or could not be deleted
    }

    private void saveBooksToSerializedFile(List<Book> books) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("medialab/books.ser"))) {
            // Load the existing list of books from the file
            List<Book> existingBooks = (List<Book>) ois.readObject();

            // Iterate through the existing list of books
            for (Book existingBook : existingBooks) {
                // Check if the current existing book exists in the updated list of books
                for (Book updatedBook : books) {
                    if (existingBook.getIsbn().equals(updatedBook.getIsbn())) {

                        // Update the existing book with the updated book data
                        existingBook.setNumCopies(updatedBook.getNumCopies());
                        System.out.println("Existing Book Copies:");
                        System.out.println(existingBook.getNumCopies());
                        break; // Break out of the loop after updating the copies
                    }
                }
            }

            // Write the updated list of books back to the file
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("medialab/books.ser"))) {
                oos.writeObject(existingBooks); // Write the existingBooks list with updated copies
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<Book> loadBooksFromSerializedFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("medialab/books.ser"))) {
            return (List<Book>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null; // Return null if there was an error or the file doesn't exist
    }



}
