package com.example.demo;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class AddCategoryController {

    @FXML
    private TextField categoryNameField;

    private List<Category> categoriesList = new ArrayList<>();
    @FXML
    private Button returnButton;

    @FXML
    private Button addButton;


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
    void addButtonClicked(ActionEvent event) {
        String categoryName = categoryNameField.getText();
        Category newCategory = new Category(categoryName);
        categoriesList.add(newCategory);
        System.out.println("Category added: " + categoryName);
        printAllCategories();
        saveCategoriesToFile();
    }

    private void printAllCategories() {
        System.out.println("All Categories:");
        for (Category category : categoriesList) {
            System.out.println(category.getName());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadCategoriesFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("medialab/categories.ser"))) {
            categoriesList = (List<Category>) ois.readObject();
            System.out.println("Categories loaded from file.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        loadCategoriesFromFile();
    }


    private void saveCategoriesToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("medialab/categories.ser"))) {
            oos.writeObject(categoriesList);
            System.out.println("Categories saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}