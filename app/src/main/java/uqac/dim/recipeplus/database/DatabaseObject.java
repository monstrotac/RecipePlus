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

public class DatabaseObject{
    //constant variables
    public static final String PASSWORD = "UQAC2022";
    public static final String USERNAME = "RECIPEPLUSROOT";
    public static final String DATABASE_URL = "jdbc:mysql://192.168.0.17:3306/recipePlusDatabase";
    //private variables
    private Connection activeConnection;
    private Statement statement;
    private ResultSet loggedInUser;
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
        return statement.executeQuery("SELECT ING.* FROM INGREDIENT ING WHERE ING.id in (SELECT LINK.ingredientId FROM RECIPE_INGREDIENT LINK WHERE LINK.recipeId = "+recipeId+")");
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
    /*
    This section is dedicated to all of the user's connection activity.
     */
    //Attempts to find the information given in the database and returns true if found. It then keeps the user in the DB.
    public Boolean attemptUserLogIn(String email, String password) throws SQLException {
        ResultSet attempt = statement.executeQuery("SELECT id, firstName, lastName FROM USER WHERE email = \"" + email + "\" AND password = \"" + password + "\"");
        while (attempt.next()){
            if (attempt.getString(1) != null){
                loggedInUser = attempt;
                return true;
            }
        }
        return false;
    }
    //Attempts to register a new user into the database with the data provided.
    public Boolean attemptUserRegister(String firstName, String lastName, String password, String email) throws SQLException {
        //Checks if the user already exists.
        ResultSet attempt = statement.executeQuery("SELECT id, firstName, lastName FROM USER WHERE email = \"" + email + "\"");

        while (attempt.next()){
            if (attempt.getString(1) != null){
                return false;
            }
        }
        statement.executeQuery("INSERT INTO USER (firstName, lastName, password, email) values ('"+firstName+"', '"+lastName+"', '"+password+"', '"+email+"');");
        return true;
    }
    //Logs out the user by returning the loggedInUser to null
    public void disconnectUser (){
        loggedInUser = null;
    }
    //Simple getter that returns the user and its informations.
    public ResultSet getLoggedInUser() {
        return loggedInUser;
    }
}
