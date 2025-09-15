package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.*;
import java.util.List;

public class BorrowBookController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label copiesLabel;

    //private List<User> users; // Add this variable to hold the list of users

    private User user;
    public void setUsers(List<User> users) {
        this.users = users;
    }
    @FXML
    private Button borrowButton;

    private List<Book> books = loadBooksFromSerializedFile();
    private List<User> users = loadUsersFromSerializedFile();
    private Book selectedBook;



    public void initData(Book book, User user) {
        this.selectedBook = book;
        this.user = user;
        updateUI();
    }

    private User updateUI() {
        titleLabel.setText(selectedBook.getTitle());
        copiesLabel.setText(String.valueOf(selectedBook.getNumCopies()));
        return null;
    }

    private static final String BOOKS_SERIALIZED_FILE = "medialab/books.ser";
    private static final String USERS_SERIALIZED_FILE = "medialab/users.ser";
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
            System.out.println("Users serialized successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private List<User> loadUsersFromSerializedFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_SERIALIZED_FILE))) {
            List<User> loadedUsers = (List<User>) ois.readObject();
            System.out.println("Users loaded successfully.");
            return loadedUsers;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null; // Return an empty list if there was an error or the file doesn't exist
    }


    @FXML
    private void handleBorrow(ActionEvent event) {
        if(LoginController.currentUser.getBorrowedBooks().size() >=2){

                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Maximum Books Borrowed");
                alert.setHeaderText("Maximum amount of books borrowed. Please return a book first!");
                alert.showAndWait();
            }
        else if (LoginController.currentUser != null && selectedBook != null) {
{
                System.out.println("Borrowing book for user: " + LoginController.currentUser.getUsername());
                System.out.println("Book Title: " + selectedBook.getTitle());

                // Borrow the book for the current user
                LoginController.currentUser.borrowBook(selectedBook);
            }
            for (User user :users) {
                if (user.getAdt() ==(LoginController.currentUser.getAdt())) {
                    // Update the book with decreased number of copies
                    user.borrowBook(selectedBook);
                    break; // No need to continue iterating once found
                }
            }
            // Update the books list to reflect the changes
            for (Book book : books) {
                if (book.getTitle().equals(selectedBook.getTitle())) {
                    // Update the book with decreased number of copies
                    book.decreaseNumCopies();
                    break; // No need to continue iterating once found
                }
            }

            // Save the updated list of books to the serialized file
            saveBooksToSerializedFile(books);
            saveUsersToSerializedFile(users);

            // Print the list of borrowed books for the user
            System.out.println("List of Borrowed Books for User: " + LoginController.currentUser.getUsername());
            for (Book book : LoginController.currentUser.getBorrowedBooks()) {
                System.out.println("- " + book.getTitle());
            }

            // Update UI accordingly if needed
        } else {
            System.out.println("Current user or selected book not found!");
            // Handle the case where the current user or selected book is not found
        }
    }







}