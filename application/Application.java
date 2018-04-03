package application;

import api.APIRequest;
import api.BusinessRequest;
import api.Restaurant;
import gui.GUI;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class Application {
    private Database db;
    private GUI gui;
    private String user;
    private Scanner keyboard;
    private LinkedHashSet<Restaurant> favorites;

    public Application() {
        this.db = new Database();
        this.keyboard = new Scanner(System.in);
        this.favorites = new LinkedHashSet<>();
    }

    /**
     * Start the app by asking the user to login/register
     * Then load the GUI and make the first API calls to show results on the UI
     */
    public void start() {
        System.out.println("Welcome to LA Foodie!");
        login();
        this.gui = new GUI(this, this.user);
        keyboard.close();
        gui.init();
        loadRestaurants();
        loadFavorites();
    }

    /**
     * Makes a request to the API then put the results onto the scroll pane
     */
    public void loadRestaurants() {
        APIRequest req = new APIRequest();
        Restaurant[] results = req.send();

        gui.renderSearchResults(results);
    }

    /**
     * Ask the database for stored favorites, then call the API to get those Restaurants and show them on the UI
     */
    public void loadFavorites() {
        ArrayList<String> favoriteNames = db.getFavorites(this.user);

        for (String name : favoriteNames) {
            BusinessRequest req = new BusinessRequest(name);
            Restaurant fave = req.send();
            this.favorites.add(fave);
        }

        gui.renderFavorites();
    }

    /**
     * Add a single Restaurant to the set of favorites
     *
     * @param newFavorite the new Restaurant to add
     */
    public void addToFavorites(Restaurant newFavorite) {
        this.favorites.add(newFavorite);
        gui.renderFavorites();
    }

    /**
     * Hash the password before storing or comparing in the database
     *
     * @param username the user's username
     * @param pw       the user's raw password
     * @return a hashed version of the password
     */
    private String hash(String username, String pw) {
        try {
            // This is called "salting" the hash
            String input = username.substring(0, 1) + pw + username.substring(1);
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(input.getBytes(), 0, input.length());
            BigInteger num = new BigInteger(1, m.digest());
            return num.toString(16);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
            System.exit(1);
            return "";
        }
    }

    private void login() {
        System.out.println("Enter 1 to log in, or 2 to register: ");
        String input = keyboard.nextLine();
        String username = "";
        String password = "";

        while (!input.equals("1") && !input.equals("2")) {
            System.out.println("Invalid choice, please enter 1 to log in or 2 to register: ");
            input = keyboard.nextLine();
        }

        // Log the user in
        if (input.equals("1")) {
            boolean authenticated = false;

            while (!authenticated) {
                System.out.println("Enter your username:");
                username = keyboard.nextLine();
                System.out.println("Enter your password:");
                password = keyboard.nextLine();
                authenticated = db.authenticateUser(username, hash(username, password));

                if (!authenticated) {
                    System.out.println("Invalid credentials, please try again");
                } else {
                    this.user = username;
                    authenticated = true;
                }
            }

            System.out.printf("Welcome back, %s!\n\n", user);
        } else {
            username = "";
            password = "";
            boolean userExists = true;

            while (userExists) {
                while (username.length() == 0) {
                    System.out.println("Enter your desired username:");
                    username = keyboard.nextLine();
                }

                userExists = db.doesUserExist(username);

                if (userExists) {
                    System.out.println("Sorry, that username is taken");
                    username = "";
                }
            }

            System.out.println("Choose a password:");
            password = keyboard.nextLine();

            while (password.length() < 6 || password.length() > 20) {
                System.out.println("Password must be between 6 and 20 characters, try again: ");
                password = keyboard.nextLine();
            }

            db.createUser(username, hash(username, password));
            this.user = username;

            System.out.printf("Welcome, %s!\n\n", user);
        }
    }

    public LinkedHashSet<Restaurant> getFavorites() {
        return favorites;
    }

    public void setFavorites(LinkedHashSet<Restaurant> favorites) {
        this.favorites = favorites;
    }

    public Database getDb() {
        return db;
    }

    public GUI getGui() {
        return this.gui;
    }

    public String getUser() {
        return user;
    }
}
