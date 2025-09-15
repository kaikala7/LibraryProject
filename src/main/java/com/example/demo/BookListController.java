package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class BookListController{

    @FXML
    private TableView<Book> tableView;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> isbnColumn;

    @FXML
    private TableColumn<Book, String> publisherColumn;

    @FXML
    private TableColumn<Book, String> categoryColumn;


    @FXML
    private TableColumn<Book, Integer> releaseYearColumn;

    @FXML
    private TableColumn<Book, Integer> numCopiesColumn;

    @FXML
    private Button returnButton;

    private List<Book> books;
    @FXML
    private TableColumn<Book, List<String>> commentsColumn;

    @FXML
    private TableColumn<Book, List<Integer>> ratingsColumn;

    @FXML
    private TableColumn<Book, Double> averageRatingColumn;

    @FXML
    private TableColumn<Book, Integer> numRatingsColumn;

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
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        releaseYearColumn.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));
        numCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("numCopies"));
        commentsColumn.setCellValueFactory(new PropertyValueFactory<>("comments"));
        ratingsColumn.setCellValueFactory(new PropertyValueFactory<>("ratings"));
        averageRatingColumn.setCellValueFactory(new PropertyValueFactory<>("averageRating"));
        numRatingsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfRatings"));

        loadBooksFromSerializedFile();

        // Wrap the existing list in a SortedList
        SortedList<Book> sortedList = new SortedList<>(FXCollections.observableArrayList(books));

        // Set the comparator to sort by category column in descending order
        sortedList.setComparator((book1, book2) -> {
            String category1 = book1.getCategory();
            String category2 = book2.getCategory();
            return category2.compareTo(category1);
        });

        tableView.setItems(sortedList);
    }

    private void loadBooksFromSerializedFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("medialab/books.ser"))) {
            books = (List<Book>) ois.readObject();
            System.out.println("Books loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); // Handle error loading books from serialized file
        }
    }
}