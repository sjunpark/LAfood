package application;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Database {
    private final String url = "jdbc:mysql://yourdatabaseurl:3306/";
    private final String username = "TO DO";
    private final String password = "TO DO";
    private final String database = "TO DO";
    private Connection conn;
    private static final String driver = "com.mysql.jdbc.Driver";

    public Database() {
        this.connect();
    }

    private boolean connect() {
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url + database, username, password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean authenticateUser(String username, String password) {
        try {
            Statement st = conn.createStatement();
            String query =  "SELECT * FROM users WHERE " +
                            "name = '" + username + "' AND " +
                            "password = '" + password + "'";
            ResultSet result = st.executeQuery(query);

            // This means we found a row matching the username/password combination
            if (result.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Failed authenticating user due to SQL error!");
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean doesUserExist(String username) {
        try {
            Statement st = conn.createStatement();
            String query =  "SELECT * FROM users WHERE name='" + username + "'";

            ResultSet result = st.executeQuery(query);

            // This user already exists
            if (result.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Failed checking for user due to SQL error!");
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public boolean createUser(String username, String password) {
        try {
            Statement st = conn.createStatement();
            String query =  "INSERT INTO users (name, password) VALUES ('" + 
                            username + "', '" + password + "')";

            int numRows = st.executeUpdate(query);

            if (numRows == 0) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Failed creating user due to SQL error!");
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean addFavorite(String userName, String restaurant) {
        try {
            Statement st = conn.createStatement();
            String query =  "INSERT INTO favorites (name, restaurant) VALUES ('" + 
                            userName + "', '" + restaurant + "')";

            int numRows = st.executeUpdate(query);

            if (numRows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Failed adding favorite due to SQL error!");
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean removeFavorite(String userName, String restaurant) {
        try {
            Statement st = conn.createStatement();
            String query =  "DELETE FROM favorites WHERE name='" + userName +  
                            "' AND restaurant='" + restaurant + "'";

            int numRows = st.executeUpdate(query);

            if (numRows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Failed removing favorite due to SQL error!");
            System.out.println(e.getMessage());
            return false;
        }
    }

    public ArrayList<String> getFavorites(String userName) {
        ArrayList<String> storedFavorites = new ArrayList<>();

        try {
            Statement st = conn.createStatement();
            String query =  "SELECT * FROM favorites WHERE name='" + userName + "'";
            ResultSet results = st.executeQuery(query);
            while (results.next()) {
                storedFavorites.add(results.getString("restaurant"));
            }

            return storedFavorites;

        } catch (SQLException e) {
            System.err.println("Failed retrieving favorites due to SQL error!");
            System.out.println(e.getMessage());
            return null;
        }
    }
}