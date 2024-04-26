package application;

import utility.TextUI;

import java.util.ArrayList;


public abstract class AMedia implements IMedia {
    public String title;
    TextUI ui = new TextUI();
    public String year;
    public ArrayList<String> category;
    public float rating;

    public AMedia(String title, String year,  ArrayList<String> category, float rating) {
        this.title = title;
        this.year = year;
        this.category = category;
        this.rating = rating;
    }

    public String getTitle(){
        return title;
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public float getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Genre: " + category + ", Rating: " + rating;
    }
}