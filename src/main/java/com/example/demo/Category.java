package com.example.demo;
import java.io.Serial;
import java.io.Serializable;

public class Category implements Serializable {

    private String name;

    // Constructors
    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Override toString() for meaningful representation
    @Override
    public String toString() {
        return name;
    }
}