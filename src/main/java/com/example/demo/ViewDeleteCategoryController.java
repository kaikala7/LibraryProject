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
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;

public class ViewDeleteCategoryController {




    @FXML
    private TableView<Category> tableView;

    @FXML
    private TableColumn<Category, String> categoryNameColumn;



    @FXML
    private Button returnButton;

    private List<Category> categories;

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
        categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Create the delete button column
        TableColumn<Category, Void> deleteButtonColumn = new TableColumn<>("Delete");
        deleteButtonColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Category cat = getTableView().getItems().get(getIndex()); // Get the user associated with this row
                    String cname = cat.getName(); // Get the adt of the user

                    if (deleteCatFromSerializedFile(cname)) {
                        // If user deletion is successful, remove the user from the TableView
                        getTableView().getItems().remove(cat);
                    } else {
                        System.out.println("Failed to delete Category with Name: " + cname);
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
        loadCategoriesFromSerializedFile();

        // Add loaded books to the table view
        tableView.getItems().addAll(categories);
    }

    private void loadCategoriesFromSerializedFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("medialab/categories.ser"))) {
            categories = (List<Category>) ois.readObject();
            System.out.println("Books loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); // Handle error loading books from serialized file
        }
    }

    private void saveCategoriesToSerializedFile(List<Category> categories) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("medialab/categories.ser"))) {
            oos.writeObject(categories);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean deleteCatFromSerializedFile(String name) {
        // Load the list of users from the serialized file
        loadCategoriesFromSerializedFile();

        if (categories != null) {
            // Iterate through the list of users and remove the user with the given ADT
            for (Category category : categories) {
                if (category.getName().equals(name)) {


                    // Remove the user from the list of users
                    categories.remove(category);

                    // Save the updated list of users back to the file
                    saveCategoriesToSerializedFile(categories);


                    return true; // Return true if the user was successfully deleted
                }
            }
        }
        return false; // Return false if the user was not found or could not be deleted
    }



}
