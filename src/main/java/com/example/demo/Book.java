package com.example.demo;
import java.io.Serializable;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import java.util.ArrayList;
import java.util.List;
public class Book implements Serializable{
    private String title;

    private List<User> borrowers;
    private String author;
    private String isbn;
    private String publisher;
    private String category;
    private int releaseYear;
    private int numCopies;

    private static List<Category> categories;
    private int numberOfRatings;

    private List<String> comments;
    private List<Integer> ratings;
    private double averageRating;

    // Constructors
    public Book() {
        // Default constructor
    }

    public Book(String title, String author, String isbn, String publisher, String category, int releaseYear, int numCopies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.category = category;
        this.releaseYear = releaseYear;
        this.numCopies = numCopies;
        this.comments = new ArrayList<>();
        this.ratings = new ArrayList<>();
        this.averageRating = 0.0;
        this.numberOfRatings = 0;
        this.borrowers = new ArrayList<>();
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void addRating(int newRating) {
        ratings.add(newRating); // Add the new rating to the list
        calculateAverageRating(); // Recalculate the average rating
    }
//    public static void addCategory(Category category) {
//      categories.add(category);
// }

//    public static List<Category> getCategories() {
//        return categories;
//    }


    public void calculateAverageRating() {
        if (ratings.isEmpty()) {
            averageRating = 0.0;
            return;
        }

        int sum = 0;
        for (int rating : ratings) {
            sum += rating;
        }

        averageRating = (double) sum / ratings.size();
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getNumCopies() {
        return numCopies;
    }

    public void setNumCopies(int numCopies) {
        this.numCopies = numCopies;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public List<Integer> getRatings() {
        return ratings;
    }

    public void setRatings(List<Integer> ratings) {
        this.ratings = ratings;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public void increaseNumRatings() {
        numberOfRatings++;
    }

    public void borrowBook(User user) {
        if (numCopies > 0) { // Check if there are copies available
            borrowers.add(user); // Add the user to the list of borrowers
            numCopies--; // Decrease the number of copies available
            System.out.println(user.getUsername() + " borrowed the book: " + title);
        } else {
            System.out.println("Sorry, the book '" + title + "' is currently not available for borrowing.");
        }
    }

    // Method to return a book
    public void returnBook(User user) {
        if (borrowers.contains(user)) { // Check if the user borrowed the book
            borrowers.remove(user); // Remove the user from the list of borrowers
            numCopies++; // Increase the number of copies available
            System.out.println(user.getUsername() + " returned the book: " + title);

        } else {
            System.out.println("User " + user.getUsername() + " did not borrow the book '" + title + "'.");
        }
    }
    public void decreaseNumCopies() {
        if (numCopies> 0) {
            numCopies--;
        }
    }

    // Method to increase the number of copies when a book is returned
    public void increaseNumCopies() {
        numCopies++;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Book otherBook = (Book) obj;
        return isbn.equals(otherBook.isbn);
    }

    // hashCode() method should also be overridden when equals() is overridden
    @Override
    public int hashCode() {
        return isbn.hashCode();
    }



}
