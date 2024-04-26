package utility;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import domain.User;

public class FileIO {

    private ArrayList<String> listOfMovies = new ArrayList<>();
    private ArrayList<String> listOfSeries = new ArrayList<>();
    private final String favoritesPath = "data/favoritesPath.csv";
    private final String watchedPath = "data/watchedPath.csv";
    private final String userSavePath = "data/UserData.csv";

    // Metode til at læse data fra fil.
    // Da håndtering af data til moviePath of seriePath
    // håndteres på samme måde, er der lavet en scanFile metode for at undgå dobbelt kode
    public ArrayList<String> readMovieData() {
        ArrayList<String> list;
        String moviePath = "data/moviePath.csv";
        list = scanFile(moviePath);

        for (String element : list) {
            listOfMovies.add(element);
        }

        return listOfMovies;
    }

    // Metode til at læse data fra fil.
    // Da håndtering af data til moviePath of seriePath
    // håndteres på samme måde, er der lavet en scanFile metode for at undgå dobbelt kode
    public ArrayList<String> readSerieData() {
        ArrayList<String> list;
        String seriePath = "data/seriePath.csv";
        list = scanFile(seriePath);

        for (String element : list) {

            listOfSeries.add(element);
        }

        return listOfSeries;
    }

    // Køre igennem en serie/movie fil, og gemmer hvert linje til en arraylist som returneres.
    private ArrayList<String> scanFile(String path) {
        ArrayList<String> list = new ArrayList<>();

        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                list.add(scanner.nextLine());
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        return list;
    }

    // Giver det nuværende user og nuværende movie objekter videre til 'mediaSaveOrRemove' (Undgår dobbeltkode)
    public void saveFavorites(User currentUser, String title) {
        mediaSaveOrRemove(currentUser, title, favoritesPath, false);

    }

    // Giver det nuværende user og nuværende movie objekter videre til 'mediaSaveOrRemove' (Undgår dobbeltkode)
    public void saveWatched(User currentUser, String title) {
        mediaSaveOrRemove(currentUser, title, watchedPath, false);

    }

    // Sletter film fra brugerens favorites via mediaSaveOrRemove
    public void deleteFavorites(String title, User currentUser) {
        mediaSaveOrRemove(currentUser, title, favoritesPath, true);
    }

    // Indlæser enten hele favorites eller watched filen, og indsætter film eller serie titlen på
    // den korrekte linje efter brugernavnet, så pågældende film/serie kan kobles til et username.
    private void mediaSaveOrRemove(User currentUser, String title, String path, boolean willRemove) {

        int counter = 0;

        try {
            //Fil indlæsning
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            FileWriter writer = new FileWriter(path, true);
            List<String> lines = Files.readAllLines(Path.of(path));

            //finder hvilken linje i filen en bruger står på
            while (scanner.hasNextLine()) {
                String[] nameSearch = scanner.nextLine().split(";");
                if (currentUser.getUsername().equals(nameSearch[0].trim())) {
                    break;
                }
                counter++;
            }
            scanner.close();

            //Skriver alle linjer fra index 0, og til (men ikke med!) brugerens index tilbage til writer
            String lineToEdit = lines.get(counter);
            for (int i = 0; i < counter; i++) {
                writer.append(lines.get(i) + "\n");
            }


            //Tilføjer titlen til brugerens favorites eller watched
            String[] arrayToEdit = lineToEdit.split(";");
            if(willRemove){
                arrayToEdit[0] += ";";
            }else{
                if(!arrayToEdit[1].contains(title)) {
                    arrayToEdit[0] += "; " + title + ",";
                }else{
                    arrayToEdit[0] += ";";
                }
            }

            // Sletter dokumentet (data dog stadig gemt i variablerne)
            PrintWriter deleteData = new PrintWriter(path);
            deleteData.print("");
            deleteData.close();

            // tilføjer resten af titlerne
            if(willRemove != true) {
                for (String element : arrayToEdit) {
                    if(!element.equals(" ")) {
                        writer.append(element);
                    }
                }
            }

            // Sletter titlen som brugeren har valgt
            if(willRemove){
                writer.append(currentUser.getUsername() + "; ");
                String[] userTitles = arrayToEdit[1].split(",");
                for (String element : userTitles) {
                    if(!element.trim().equals(title) && !element.equals(" ")) {
                        writer.append(element.trim() + ", ");
                    }
                }
            }

            // Skriver alle linjer fra (men ikke med!) brugerens index til slut af arrayet tilbage til writer
            for (int i = counter + 1; i < lines.size(); i++) {
                writer.append("\n" + lines.get(i));
            }

            // skriver alt gemt data til filen
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // Henter User favorites via 'getMedia' (Undgår dobbeltkode)
    public ArrayList<String> getFavorites(User currentUser) {
        return getMedia(currentUser, favoritesPath);
    }

    // Henter User watched via 'getMedia' (Undgår dobbeltkode)
    public ArrayList<String> getWatched(User currentUser) {
        return getMedia(currentUser, watchedPath);

    }

    // Leder efter userName i favorites/watched filen, og ud fra username returnere
    // deres favorites/watched film/movies
    private ArrayList<String> getMedia(User currentUser, String path) {
        File file = new File(path);
        ArrayList<String> list = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(file);
            String str;

            // While loopet og if statement prøver at finde brugeren i filen
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] nameSearch = line.split(";");

                // returnere alle brugerens favorites/watched
                if (currentUser.getUsername().equals(nameSearch[0].trim())) {
                    String[] titles = nameSearch[1].split(",");
                    for (String s : titles) {
                        list.add(s.trim());
                    }
                    return list;
                }
            }
            scanner.close();

            return null;

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return null;
        }
    }

    // Gemmer brugerens username og password i userdata, samt gemmer
    // username i favorites og watched filerne.
    public void saveUserData(User currentUser) {
        try {
            // Når der bliver lavet en bruger, bliver der lavet et entry i 'userSavePath',
            // 'favoritesPath' og 'watchedPath'. Dette er for at være sikker på at brugeren
            // eksistere i alle de nødvendige filer
            FileWriter userWriter = new FileWriter(userSavePath, true);
            FileWriter favoritesWriter = new FileWriter(favoritesPath, true);
            FileWriter watchedWriter = new FileWriter(watchedPath, true);

            userWriter.append("\n" + currentUser.getUsername() + ", " + currentUser.getPassword());
            favoritesWriter.append("\n" + currentUser.getUsername() + "; ");
            watchedWriter.append("\n" + currentUser.getUsername() + "; ");

            userWriter.close();
            favoritesWriter.close();
            watchedWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<User> readUserData() {
        ArrayList<User> userList = new ArrayList<>();
        ArrayList<String> data = scanFile(userSavePath);

        for (String line : data) {
            String[] parts = line.split(", ");
            if (parts.length >= 2) {
                String username = parts[0].trim();
                String password = parts[1].trim();

                User user = new User(username, password);

                userList.add(user);
            } else {
                System.out.println("Invalid data format: " + line);
            }
        }
        return userList;
    }
}
