package com.example.demo;

import java.util.List;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;
public class DetailedBookInfoController {

    @FXML
    private TableView<Book> bookDetailsTable;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> isbnColumn;

    @FXML
    private TableColumn<Book, String> avgRatingColumn;

    @FXML
    private TableColumn<Book, String> noRatingsColumn;

    @FXML
    private TableColumn<Book, String> publisherColumn;

    @FXML
    private TableColumn<Book, String> releaseYearColumn;

    @FXML
    private TableColumn<Book, String> categoryColumn;

    @FXML
    private TableColumn<Book, String> noCopiesColumn;

    @FXML
    private TableColumn<Book, String> commentsColumn;
    private Book book;
    public void initData(Book book) {
        this.book = book;
        // Use the book data to initialize the UI elements
    }
    // Method to initialize the controller
    @FXML
    private void initialize() {
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        authorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));
        isbnColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIsbn()));
        avgRatingColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getAverageRating())));
        noRatingsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumberOfRatings())));

        publisherColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPublisher()));
        releaseYearColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getReleaseYear())));
        categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        noCopiesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumCopies())));
        commentsColumn.setCellValueFactory(cellData -> {
            List<String> commentsList = cellData.getValue().getComments();
            if (commentsList != null && !commentsList.isEmpty()) {
                StringBuilder commentsBuilder = new StringBuilder();
                for (String comment : commentsList) {
                    commentsBuilder.append(comment).append("\n"); // Add newline separator
                }
                return new SimpleStringProperty(commentsBuilder.toString().trim());
            } else {
                return new SimpleStringProperty(""); // Return empty string if comments list is null or empty
            }
        });
    }

    // Method to set the book details to display in the table
    public void setBookDetails(Book book) {
        if (book != null) {
            System.out.println("Received book: " + book.getTitle()); // Check if the book object is not null
        } else {
            System.out.println("Book object is null");
        }
        bookDetailsTable.getItems().clear();
        bookDetailsTable.getItems().add(book);
    }

}
