package com.example.demo;
import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.*;
import java.util.List;
/**
 * Represents a user of the system.
 */
public class User implements Serializable {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private int adt;
    private boolean isAdmin;
    private List<Book> borrowedBooks;
    private static final String BOOKS_SERIALIZED_FILE = "medialab/books.ser";
    private static final String USERS_SERIALIZED_FILE = "medialab/users.ser";

    /**
     * Constructor to create a new User.
     * @param username The username of the user.
     * @param password The password of the user.
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param adt The adt of the user.
     * @param email The email of the user.
     * @param isAdmin Whether the user is an administrator.
     */
    public User(String username, String password, String firstName, String lastName, int adt, String email, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.adt = adt;
        this.email = email;
        this.isAdmin = false;
        this.borrowedBooks = new ArrayList<>();
    }

    /**
     * Retrieves the username of the user.
     * @return The username of the user.
     */
    // Getters and Setters
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     * @param username The new username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieves the password of the user.
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     * @param password The new password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retrieves the first name of the user.
     * @return The first name of the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     * @param firstName The new first name to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Retrieves the last name of the user.
     * @return The last name of the user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     * @param lastName The new last name to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Retrieves the email of the user.
     * @return The email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     * @param email The new email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * Retrieves the adt of the user.
     * @return The adt of the user.
     */
    public int getAdt() {
        return adt;
    }

    /**
     * Sets the adt of the user.
     * @param adt The new adt to set.
     */
    public void setAdt(int adt) {
        this.adt = adt;
    }

    /**
     * Checks whether the user is an administrator.
     * @return True if the user is an administrator, otherwise false.
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Sets whether the user is an administrator.
     * @param isAdmin True if the user is an administrator, otherwise false.
     */
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * Retrieves the list of borrowed books by the user.
     * @return The list of borrowed books.
     */
    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }
    /**
     * Sets the list of borrowed books by the user.
     * @param borrowedBooks The new list of borrowed books.
     */
    public void setBorrowedBooks(List<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    /**
     * Returns a string representation of the User object.
     * @return A string representation of the User object.
     */
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", adt='" + adt + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    /**
     * Allows the user to borrow a book.
     * Adds the book to the user's borrowedBooks list.
     * @param book The book to be borrowed.
     */
    public void borrowBook(Book book) {
        if (book.getNumCopies() > 0) {
            borrowedBooks.add(book);
        } else {
            // Create and configure the alert
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Book Not Available");
            alert.setHeaderText(null);
            alert.setContentText("Sorry, the book '" + book.getTitle() + "' is currently not available.");
            alert.showAndWait();
        }
    }

    /**
     * Allows the user to return a borrowed book.
     * Removes the book from the user's borrowedBooks list.
     * @param book The book to be returned.
     */
    public void returnBook(Book book) {
            borrowedBooks.remove(book);
            System.out.println("Book returned: " + book.getTitle());
        System.out.println(getBorrowedBooks());
        System.out.println(book.getTitle());
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
        return null; //
    }
}

