package com.example.demo;

public class BorrowedBook {
    private User user;
    private String bookTitle;

    public BorrowedBook(User user, String bookTitle) {
        this.user = user;
        this.bookTitle = bookTitle;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
}