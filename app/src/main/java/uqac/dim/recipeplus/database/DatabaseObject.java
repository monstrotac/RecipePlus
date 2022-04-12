package uqac.dim.recipeplus.database;

/*
    DOCUMENTATION FOR DATABASE OPERATIONS WITH JDBC:
    https://www.javatpoint.com/steps-to-connect-to-the-database-in-java
    https://stackoverflow.com/questions/2839321/connect-java-to-a-mysql-database
 */

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseObject implements Serializable {
    //constant variables
    public static final String PASSWORD = "UQAC2022";
    public static final String USERNAME = "RECIPEPLUSROOT";
    public static final String DATABASE_URL = "jdbc:mysql://192.168.0.17:3306/recipePlusDatabase";
    //private variables
    private Connection activeConnection;
    private Statement statement;
    //public variables

    //Constructors
    public DatabaseObject() throws SQLException {
        activeConnection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        System.out.println("Database connected!");
        statement = activeConnection.createStatement();
    }
    public DatabaseObject(String username, String password) throws SQLException {
        activeConnection = DriverManager.getConnection(DATABASE_URL, username, password);
        System.out.println("Database connected!");
        statement = activeConnection.createStatement();
    }
    public DatabaseObject(String url, String username, String password) throws SQLException {
        activeConnection = DriverManager.getConnection(url, username, password);
        System.out.println("Database connected!");
        statement = activeConnection.createStatement();
    }

    /*
    This sections is dedicated to all of the select requests, most of them are used to render data on the front-end side.
     */

    //Selects all recipes
    public ResultSet getAllRecipes(boolean orderAscending) throws SQLException {
        if(orderAscending)
            return statement.executeQuery("SELECT * FROM RECIPE ORDER BY id ASC");
        else return statement.executeQuery("SELECT * FROM RECIPE ORDER BY id DESC");
    }
    //Selects all existing ingredients in the database
    public ResultSet getAllIngredients() throws SQLException {
        return statement.executeQuery("SELECT * FROM INGREDIENT ORDER BY id DESC");
    }
    //Selects all ingredient association link associated to the recipeid given
    public ResultSet getIngredientsAssociationWithRecipeId(int recipeId) throws SQLException {
        return statement.executeQuery("SELECT * FROM RECIPE_INGREDIENT WHERE recipeId = " + recipeId);
    }
    //Selects all ingredients associated with result RECIPE_INGREDIENT select with given recipeID
    public ResultSet getRecipeIngredients(int recipeId) throws SQLException {
        return statement.executeQuery("SELECT ING.* FROM INGREDIENT ING LINK WHERE ING.id in (SELECT LINK.ingredientId FROM RECIPE_INGREDIENT WHERE LINK.recipeId = "+recipeId+")");
    }
    //Selects all user favourited recipes corresponding to the ID of the user given.
    public ResultSet getUserFavouriteRecipes(int userid) throws SQLException {
        return statement.executeQuery("SELECT * FROM USER_FAVORITE WHERE userId = " + userid);
    }
    //Selects the thumbnail of the recipe corresponding to the supplied ID.
    public ResultSet getRecipeThumbnail(int recipeId) throws SQLException {
        return statement.executeQuery("SELECT image FROM RECIPE_THUMBNAIL WHERE recipeId = " + recipeId);
    }
    //Selects the image of the user corresponding to the supplied ID
    public ResultSet getUserImage(int userId) throws SQLException {
        return statement.executeQuery("SELECT image FROM USER_IMAGE WHERE userId = " + userId);
    }

    public void Booga(){
        System.out.println("Connecting database...");

        System.out.println("Loading driver...");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded!");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find the driver in the classpath!", e);
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.0.17:3306/recipePlusDatabase", "RECIPEPLUSROOT", "UQAC2022")) {
            System.out.println("Database connected!");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select * from RECIPE");
            while(rs.next()){
                System.out.println(rs.getInt(1));
                System.out.println(rs.getString(2));
                System.out.println(rs.getString(3));
                System.out.println(rs.getString(4));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }


}
