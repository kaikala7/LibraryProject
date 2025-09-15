package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.Stage;
import javafx.scene.Scene;
public class SearchBooksController {

    @FXML
    private TextField titleTextField;

    private User currentUser;
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    private User getCurrentUser() {
        return currentUser;
    }
    @FXML
    private TextField authorTextField;

    @FXML
    private TextField releaseYearTextField;


    @FXML
    private Button searchButton;

    @FXML
    private TableView<Book> booksTableView;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> isbnColumn;

    @FXML
    private TableColumn<Book, String> avgRatingColumn;

    @FXML
    private TableColumn<Book, String> noRatings;

    @FXML private Button returnButton;
    @FXML
    private void initialize() {
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        authorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));
        isbnColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIsbn()));
        avgRatingColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getAverageRating())));
        noRatings.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumberOfRatings())));

        addButtonToTable("Show Info", this::handleShowInfo);
        addButtonToTable("Borrow", this::handleBorrow);
    }

    private void addButtonToTable(String text, EventHandler<ActionEvent> eventHandler) {
        TableColumn<Book, Void> colBtn = new TableColumn<>(text);

        Callback<TableColumn<Book, Void>, TableCell<Book, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Book, Void> call(final TableColumn<Book, Void> param) {
                final TableCell<Book, Void> cell = new TableCell<>() {
                    private final Button btn = new Button(text);

                    {
                        btn.setOnAction(eventHandler); // Assign the event handler here
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);
        booksTableView.getColumns().add(colBtn);
    }

    private void handleShowInfo(ActionEvent event) {
        Book selectedBook = booksTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("detailed_book_info.fxml"));
                Parent root = loader.load();

                // Get the controller
                DetailedBookInfoController controller = loader.getController();

                // Pass the selected book to the controller
                controller.initData(selectedBook);

                // Set the book details to display in the table
                controller.setBookDetails(selectedBook);

                // Create a new stage for the detailed book info
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    private void handleBorrow(ActionEvent event) {
        Book selectedBook = booksTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            // Retrieve the current user's information
            User currentUser = getCurrentUser(); // Make sure this method returns the current user

            // Navigate to the borrow screen with the selected book and the current user
            openBorrowScreen(selectedBook, currentUser);
        }
    }


    private void openBorrowScreen(Book selectedBook, User currentUser) {
        // Use FXMLLoader to load the borrow screen FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("borrowBook.fxml"));
        Parent root;
        try {
            root = loader.load();
            BorrowBookController controller = loader.getController();

            // Pass both the selected book and the current user to the borrow screen controller
            controller.initData(selectedBook, currentUser);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void searchBooks() {
        String title = titleTextField.getText().trim();
        String author = authorTextField.getText().trim();
        String releaseYear = releaseYearTextField.getText().trim();

        List<Book> matchingBooks = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("medialab/books.ser"))) {
            List<Book> allBooks = (List<Book>) ois.readObject();
            for (Book book : allBooks) {
                boolean matchesTitle = title.isEmpty() || book.getTitle().toLowerCase().contains(title.toLowerCase());
                boolean matchesAuthor = author.isEmpty() || book.getAuthor().toLowerCase().contains(author.toLowerCase());
                boolean matchesReleaseYear = releaseYear.isEmpty() || Integer.toString(book.getReleaseYear()).equals(releaseYear);

                if (matchesTitle && matchesAuthor && matchesReleaseYear) {
                    matchingBooks.add(book);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        booksTableView.getItems().clear();
        booksTableView.getItems().addAll(matchingBooks);
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


}
