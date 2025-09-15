package com.example.demo;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ComboBox;
public class AddBookController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField isbnField;

    @FXML
    private TextField publisherField;



    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private TextField yearField;

    @FXML
    private TextField noCopies;

    @FXML
    private Button addBook;

    @FXML
    private Button returnButton;

    private List<Book> books = new ArrayList<>();
    private List<Category> categories = new ArrayList<>(); // List to hold categories

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
    void initialize() {
        loadBooks();
        loadCategories(); // Load existing categories
        populateCategoryComboBox(); // Populate the category dropdown
    }

    private void loadCategories() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("medialab/categories.ser"))) {
            categories = (List<Category>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); // Handle error loading categories from serialized file
        }
    }

    private void populateCategoryComboBox() {
        ObservableList<String> categoryNames = FXCollections.observableArrayList();
        for (Category category : categories) {
            categoryNames.add(category.getName());
        }
        categoryComboBox.setItems(categoryNames);
    }

    @FXML
    void addBookButtonAction(ActionEvent event) {
        String title = titleField.getText();
        String author = authorField.getText();
        String isbn = isbnField.getText();
        String publisher = publisherField.getText();
        String category = categoryComboBox.getValue();
        String yearStr = yearField.getText();
        int year = Integer.parseInt(yearStr); // Convert year string to int

        String numCopiesStr = noCopies.getText(); // Get the text from numCopies field
        int numCopiesToAdd = Integer.parseInt(numCopiesStr); // Convert numCopies string to int

        // Deserialize existing list of books
        List<Book> books = loadBooks();

        // Check if the book with the given ISBN already exists in the database
        boolean bookExists = false;

        // Create a new book
        Book newBook = new Book(title, author, isbn, publisher, category, year, numCopiesToAdd);

        // Check if the book already exists
        for (Book book : books) {
            if (book.getIsbn().equals(newBook.getIsbn())) {
                // Book with the same ISBN found, update the number of copies
                int existingCopies = book.getNumCopies(); // Get the existing number of copies
                book.setNumCopies(existingCopies + numCopiesToAdd); // Add the new copies to the existing ones
                bookExists = true;
                break;
            }
        }

        // Add the new book to the list if it doesn't exist
        if (!bookExists) {
            books.add(newBook);
        }

        // Serialize the updated list of books
        saveBooks(books);
    }

    private List<Book> loadBooks() {
        List<Book> books = new ArrayList<>();
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("medialab/books.ser"))) {
            books = (List<Book>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading books from file: " + e.getMessage());
        }
        return books;
    }

    private void saveBooks(List<Book> books) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("medialab/books.ser"))) {
            objectOutputStream.writeObject(books);
            System.out.println("Book information successfully written to file.");
        } catch (IOException e) {
            System.err.println("Error writing book information to file: " + e.getMessage());
        }
    }
}
