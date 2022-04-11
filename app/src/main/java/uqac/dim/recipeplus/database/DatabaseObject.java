package uqac.dim.recipeplus.database;

/*
    DOCUMENTATION FOR DATABASE OPERATIONS WITH JDBC:
    https://www.javatpoint.com/steps-to-connect-to-the-database-in-java
    https://stackoverflow.com/questions/2839321/connect-java-to-a-mysql-database
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseObject {

    //The forName() method of Class class is used to register the driver class. This method is used to dynamically load the driver class.
    public static void forName(String className) throws ClassNotFoundException {
        //Here, Java program is loading oracle driver to establish database connection.
        Class.forName("oracle.jdbc.driver.OracleDriver");
    }

    //The getConnection() method of DriverManager class is used to establish connection with the database.
    public static Connection getConnection(String username, String password)throws SQLException{
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/recipePlusDatabase",username,password);
    }
    public static Connection getConnection(String url, String username, String password) throws SQLException
    {
        return DriverManager.getConnection(url,username,password);
    }

}
