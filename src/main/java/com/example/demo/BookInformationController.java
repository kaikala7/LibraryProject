package com.example.demo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.*;
import java.util.List;

public class BookInformationController {

    private Book book;
    private List<Book> books;

    @FXML
    private TextField titleLabel;

    @FXML
    private TextField authorLabel;

    @FXML
    private TextField isbnLabel;

    @FXML
    private TextField publisherLabel;

    @FXML
    private TextField categoryLabel;

    @FXML
    private TextField releaseYearLabel;

    @FXML
    private TextField numCopiesLabel;

    @FXML
    private Button saveButton;

    public void initData(Book book, List<Book> books) {
        this.book = book;
        this.books = books;
        titleLabel.setText(book.getTitle());
        authorLabel.setText(book.getAuthor());
        isbnLabel.setText(book.getIsbn());
        publisherLabel.setText(book.getPublisher());
        categoryLabel.setText(book.getCategory());
        releaseYearLabel.setText(String.valueOf(book.getReleaseYear()));
        numCopiesLabel.setText(String.valueOf(book.getNumCopies()));
    }

    @FXML
    private void saveChangesClicked() {
        String newTitle = titleLabel.getText();
        String newAuthor = authorLabel.getText();
        String newIsbn = isbnLabel.getText();
        String newPublisher = publisherLabel.getText();
        String newCategory = categoryLabel.getText();
        int newReleaseYear = Integer.parseInt(releaseYearLabel.getText());
        int newNumCopies = Integer.parseInt(numCopiesLabel.getText());

        book.setTitle(newTitle);
        book.setAuthor(newAuthor);
        book.setIsbn(newIsbn);
        book.setPublisher(newPublisher);
        book.setCategory(newCategory);
        book.setReleaseYear(newReleaseYear);
        book.setNumCopies(newNumCopies);

        updateBookInList(book);

        serializeBooks();

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

    private void updateBookInList(Book updatedBook) {
        for (Book b : books) {
            if (b.getIsbn().equals(updatedBook.getIsbn())) {
                b.setTitle(updatedBook.getTitle());
                b.setAuthor(updatedBook.getAuthor());
                b.setPublisher(updatedBook.getPublisher());
                b.setCategory(updatedBook.getCategory());
                b.setReleaseYear(updatedBook.getReleaseYear());
                b.setNumCopies(updatedBook.getNumCopies());
                break;
            }
        }
    }

    private void serializeBooks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("medialab/books.ser"))) {
            oos.writeObject(books);
            System.out.println("Books serialized successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
