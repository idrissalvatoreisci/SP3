package application;

import domain.User;

import java.util.ArrayList;
import java.util.Map;


public class Movie extends AMedia {
    private int seasonNumber;
    private int episodeNumber;


    public Movie(String title, String year, ArrayList<String> category, float rating) {
        super(title, year, category, rating);

        //this.seasons = seasons;
        // this.episode = episode;
    }


    public int getSeasonNumber() {
        return seasonNumber;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public String toString() {
        return title + " Release year: " + year;

    }

}