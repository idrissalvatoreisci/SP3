package domain;

import application.AMedia;
import application.Movie;
import utility.FileIO;
import utility.TextUI;

import java.util.ArrayList;
import java.util.LinkedHashSet;


public class User {
    private String username;
    private String password;
    private LinkedHashSet<AMedia> watchedList = new LinkedHashSet<>();
    private LinkedHashSet<AMedia> savedList = new LinkedHashSet<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;

    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    public void viewWatchedList() {
        for (AMedia option : watchedList) {
            System.out.println(option);
        }

    }
    public void viewSavedList() {
        for (AMedia option : savedList) {
            System.out.println(option);
        }

    }

}
