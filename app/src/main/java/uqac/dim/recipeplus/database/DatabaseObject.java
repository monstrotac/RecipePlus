package uqac.dim.recipeplus.database;

/*
    DOCUMENTATION FOR DATABASE OPERATIONS WITH JDBC:
    https://www.javatpoint.com/steps-to-connect-to-the-database-in-java
    https://stackoverflow.com/questions/2839321/connect-java-to-a-mysql-database
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uqac.dim.recipeplus.Ingredient;
import uqac.dim.recipeplus.R;
import uqac.dim.recipeplus.Recipe;
import uqac.dim.recipeplus.User;

public class DatabaseObject {
    //constant variables
    protected static final String PASSWORD = "UQAC2022";
    protected static final String USERNAME = "RECIPEPLUSROOT";
    protected static final String DATABASE_URL = "jdbc:mysql://192.168.0.17:3306/recipePlusDatabase";
    //private variables
    private Connection activeConnection;
    private Statement statement;
    private ResultSet activeUserResultSet;
    private User loggedInUserObject;
    //public variables

    //Constructors
    public DatabaseObject() throws SQLException {
        activeConnection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        System.out.println("Database connected!");
        statement = activeConnection.createStatement();
        //annihilateUsers();
        //annihilateRecipes();
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
    //Selects the user representing the provided id.
    public ResultSet getRecipeCreator(int creatorId) throws SQLException {
        return statement.executeQuery("SELECT firstName, lastName FROM USER WHERE id = "+ creatorId);
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
    //Selects the thumbnail of the recipe corresponding to the supplied ID.
    public byte[] getRecipeThumbnail(int recipeId) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT image FROM RECIPE_THUMBNAIL WHERE recipeId = " + recipeId);
        if(rs.next()){
            byte[] image = rs.getBytes(1);
            return image;
        }
        else return null;
    }
    //Selects all of the images corresponding to the recipe and returns them.
    public List<byte[]> getRecipeImage(int recipeId) throws SQLException {
        ResultSet imageSet = statement.executeQuery("SELECT image FROM RECIPE_IMAGE WHERE recipeId = " + recipeId);
        List<byte[]> imageList = new ArrayList<byte[]>();
        while (imageSet.next()){
            imageList.add(imageSet.getBytes(1));
        }
        return imageList;
    }
    //Selects the image of the user corresponding to the supplied ID
    public byte[] getUserImage(int userId) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT image FROM USER_IMAGE WHERE userId = " + userId);
        if(rs.next()){
            byte[] image = rs.getBytes(1);
            return image;
        }
        else return null;
    }
    //Selects and returns all recipes created by selected user
    public ResultSet getUserRecipes(int userId) throws SQLException {
        return statement.executeQuery("SELECT * FROM RECIPE WHERE creatorId = " + userId);
    }
    //Selects and returns all of the current logged in user's recipes
    public ResultSet getLoggedInUsersRecipes() throws SQLException {
        return statement.executeQuery("SELECT * FROM RECIPE WHERE creatorId = " + loggedInUserObject.getId());
    }

    /*
    This section is dedicated to all INSERT, UPDATE and DELETES.
    MOST OF THESE FUNCTIONS RETURN A BOOL CORRESPONDING TO IF THE OPERATION WAS A SUCCESS OR NOT.
     */

    //Adds a new recipe and links images to it, the creatorId will be the user who is logged in.
    public Boolean addNewRecipeFromLoggedInUser(Recipe recipe, byte[] recipeThumbnail, List<byte[]> recipePictures) throws SQLException {
        String[] generatedColumns = {"id"};
        statement.executeUpdate("INSERT INTO RECIPE (creatorId, name, description, instruction) values ("+loggedInUserObject.getId()+", '"+recipe.getName()+"', '"+recipe.getDesc()+"', '"+recipe.getInstruc()+"')", generatedColumns);
        int newRecipeId;
        ResultSet rs = statement.getGeneratedKeys(); //We acquire data
        if (rs.next())
            newRecipeId = rs.getInt(1); //With the help of a little Geomancy, we acquire the ID from the newest recipe.
        else
            return false;//If the INSERT failed, we will end up with nothing. Therefore we return false.
        //We insert the thumbnail data into the database, but first we prepare the data.
        PreparedStatement thumbnailStatement = activeConnection.prepareStatement("INSERT INTO RECIPE_THUMBNAIL (recipeId, image) VALUES(?,?)");
        thumbnailStatement.setInt(1, newRecipeId);
        thumbnailStatement.setBytes(2, recipeThumbnail);
        thumbnailStatement.executeUpdate();
        thumbnailStatement.close();
        //We insert all of the pictures inside the list provided in the params inside of the database.
        for (byte[] picture:recipePictures) {
            PreparedStatement pictureStatement = activeConnection.prepareStatement("INSERT INTO RECIPE_IMAGE (recipeId, image) VALUES(?,?)");
            pictureStatement.setInt(1, newRecipeId);
            pictureStatement.setBytes(2, picture);
            pictureStatement.executeUpdate();
            pictureStatement.close();
        }
        //We insert all of the ingredient associated with the recipe.
        for (Ingredient ingredient: recipe.getIngredients() ) {
            statement.executeUpdate("INSERT INTO RECIPE_INGREDIENT (ingredientId, recipeId) values ("+ingredient.getId()+", "+ newRecipeId);
        }
        return true;
    }
    //Update the given recipe
    public Boolean updateRecipe(Recipe recipe, byte[] recipeThumbnail, List<byte[]> recipePictures, List<Ingredient> ingredientList) throws SQLException {
        //First of all, we make sure that the creator of the recipe is in fact the user we are authenticated as.
        ResultSet review = statement.executeQuery("SELECT creatorId FROM RECIPE WHERE id = " + recipe.getId());
        if(review.next()){
            if(review.getInt(1) != loggedInUserObject.getId())
                return false; //If the creator doesn't correspond to the one of the Recipe cancel everything.
        } else
            return false; //If we end up here it means the Database didn't find the recipe
        //We start by updating the recipe.
        statement.executeUpdate("UPDATE RECIPE SET name='"+recipe.getName()+"',description='"+recipe.getDesc()+"',instruction='"+recipe.getInstruc()+"' WHERE id = "+recipe.getId());
        //We update the thumbnail data.
        statement.executeUpdate("UPDATE RECIPE_THUMBNAIL image="+recipeThumbnail+" WHERE recipeId="+recipe.getId());

        /*
        LOTS OF HARDCORE DEBUGGING TO DO HERE I HAVE TO MAKE SURE THAT THE DATABASE WORKS FIRST.
        //We insert all of the pictures inside the list provided in the paramas inside of the database.
        for (Byte[] picture:recipePictures) {
            statement.executeUpdate("INSERT INTO RECIPE_IMAGE (recipeId, image) values ("+ newRecipeId +", '"+picture+"')");
        }
        //We insert all of the ingredient associated with the recipe.
        for (Ingredient ingredient: ingredientList ) {
            statement.executeUpdate("INSERT INTO RECIPE_INGREDIENT (ingredientId, recipeId) values ("+ingredient.getId()+", "+ newRecipeId);
        }
        */
        return true;
    }
    //Delete the recipe with Id.
    public Boolean deleteRecipeWithId(int recipeId) throws SQLException {
        ResultSet review = statement.executeQuery("SELECT creatorId FROM RECIPE WHERE id = " + recipeId);
        if(review.next()){
            if(review.getInt(1) != loggedInUserObject.getId())
                return false; //If the creator doesn't correspond to the one of the Recipe cancel everything.
        } else
            return false; //If we end up here it means the Database didn't find the recipe to delete.

        statement.executeUpdate("DELETE FROM RECIPE_IMAGE WHERE id = " + recipeId);
        statement.executeUpdate("DELETE FROM RECIPE_THUMBNAIL WHERE id = " + recipeId);
        statement.executeUpdate("DELETE FROM RECIPE_INGREDIENT WHERE recipeId = " + recipeId);
        statement.executeUpdate("DELETE FROM RECIPE WHERE id = " + recipeId);
        return true;
    }


    /*
    This section is dedicated to all of the user's connection activity.
     */

    //Attempts to find the information given in the database and returns true if found. It then keeps the user in the DB.
    public Boolean attemptUserLogIn(String email, String password) throws SQLException {
        ResultSet attempt = statement.executeQuery("SELECT id, firstName, lastName, email FROM USER WHERE email = \"" + email + "\" AND password = \"" + password + "\"");
        while (attempt.next()){
            if (attempt.getString(1) != null){
                activeUserResultSet = attempt;
                assembleUserObject();
                return true;
            }
        }
        return false;
    }

    //Attempts to register a new user into the database with the data provided.
    public boolean attemptUserRegister(String firstName, String lastName, String password, String email, Bitmap bitmap) throws SQLException {
        //Checks if the user already exists if it does, return false and prevent user creation.
        if(!firstName.contentEquals("") && !lastName.contentEquals("") && !password.contentEquals("") && !email.contentEquals("")){
            ResultSet attempt = statement.executeQuery("SELECT id, firstName, lastName FROM USER WHERE email = \"" + email + "\"");
            while (attempt.next()){
                if (attempt.getString(1) != null){
                    return false;
                }
            }
            //Creates the user and returns true to notice that the user creation was a success.
            String[] generatedColumns = {"id"};
            statement.executeUpdate("INSERT INTO USER (firstName, lastName, password, email) values ('"+firstName+"', '"+lastName+"', '"+password+"', '"+email+"')", generatedColumns);
            int newUserId;
            ResultSet rs = statement.getGeneratedKeys(); //We acquire data
            if (rs.next())
                newUserId = rs.getInt(1); //With the help of a little Geomancy, we acquire the ID from the newest recipe.
            else
                return false;//If the INSERT failed, we will end up with nothing. Therefore we return false.

            //We create a default profile picture.
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] image = stream.toByteArray();
            //We prepare the statement to then insert bytes in the database.
            PreparedStatement userImageStatement = activeConnection.prepareStatement("INSERT INTO USER_IMAGE (userId, image) VALUES(?,?)");
            userImageStatement.setInt(1, newUserId);
            userImageStatement.setBytes(2, image);
            userImageStatement.executeUpdate();
            userImageStatement.close();
            return true;
        } else {
            return false;
        }
    }
    
    private void assembleUserObject() throws SQLException {
        loggedInUserObject = new User(activeUserResultSet.getInt(1), activeUserResultSet.getString(2), activeUserResultSet.getString(3), activeUserResultSet.getString(4));
    }

    //Logs out the user by returning the loggedInUser to null
    public void disconnectUser (){
        activeUserResultSet = null;
        loggedInUserObject = null;
    }

    //Simple getter that returns the user and its informations.
    public ResultSet getActiveUserResultSet() {
        return activeUserResultSet;
    }
    public User getLoggedInUserObject() {
        return loggedInUserObject;
    }
    public void setUser(User user){
        loggedInUserObject = user;
    }
    //These functions are used for debugging purpose and I do not recommend using them.
    private void annihilateRecipes() throws SQLException {
        statement.executeUpdate("DELETE FROM RECIPE_IMAGE");
        statement.executeUpdate("DELETE FROM RECIPE_THUMBNAIL");
        statement.executeUpdate("DELETE FROM RECIPE_INGREDIENT");
        statement.executeUpdate("DELETE FROM RECIPE");
    }
    private void annihilateUsers() throws SQLException {
        statement.executeUpdate("DELETE FROM USER_IMAGE");
        statement.executeUpdate("DELETE FROM USER");
    }
}
