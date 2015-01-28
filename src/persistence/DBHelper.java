package persistence;

import org.sqlite.SQLiteConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

/**
 * Some helper methods to create Connections or create schemas
 */
public class DBHelper {
    /**
     * Returns a Connection to the Database 
     */
    public static Connection getConnection() throws SQLException {
        // == TODO: implement this method
        //throw new RuntimeException("Please implement this method!");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        System.out.println("Opened database successfully");
        return conn;
    }
    
    /**
     * Creates all tables necessary for this task 
     */
     public static void createSchema(Connection conn) throws SQLException {
        // == TODO: implement this method
        /*
         * == Tables to be created:
         * 
         * CREATE TABLE customer(
         *     email VARCHAR(255) NOT NULL PRIMARY KEY,
         *     name VARCHAR(255) NOT NULL,
         *     password VARCHAR(255) NOT NULL
         * )
         *
         * CREATE TABLE product(
         *     prod_nr INTEGER NOT NULL PRIMARY KEY,
         *     name VARCHAR(255) NOT NULL,
         *     price INTEGER NOT NULL
         * )
         *
         * CREATE TABLE shopping_basket(
         *     id INTEGER NOT NULL PRIMARY KEY,
         *     customer VARCHAR(255) NOT NULL REFERENCES customer
         * )
         *
         * CREATE TABLE product_in_shopping_basket(
         *     product INTEGER NOT NULL REFERENCES product,
         *     shopping_basket INTEGER NOT NULL REFERENCES shopping_basket,
         *     number INTEGER NOT NULL CHECK (number > 0)
         * )"
         */
         //throw new RuntimeException("Please implement this method!");

         String tableCustomer = "CREATE TABLE customer(\n" +
                                "email VARCHAR(255) NOT NULL PRIMARY KEY,\n" +
                                "name VARCHAR(255) NOT NULL,\n" +
                                "password VARCHAR(255) NOT NULL\n" +
                                ")";

         String tableProduct = "CREATE TABLE product(\n" +
                                "prod_nr INTEGER NOT NULL PRIMARY KEY,\n" +
                                "name VARCHAR(255) NOT NULL,\n" +
                                "price INTEGER NOT NULL\n" +
                                ")";

         String tableShoppingBasket = "CREATE TABLE shopping_basket(\n" +
                                "id INTEGER NOT NULL PRIMARY KEY,\n" +
                                "customer VARCHAR(255) NOT NULL REFERENCES customer\n" +
                                ")";

         String tableProductInShoppingBasket = "CREATE TABLE product_in_shopping_basket(\n" +
                                "product INTEGER NOT NULL REFERENCES product,\n" +
                                "shopping_basket INTEGER NOT NULL REFERENCES shopping_basket,\n" +
                                "number INTEGER NOT NULL CHECK (number > 0)\n" +
                                ")";

         Statement statement = conn.createStatement();

         statement.execute(tableCustomer);
         statement.execute(tableProduct);
         statement.execute(tableShoppingBasket);
         statement.execute(tableProductInShoppingBasket);

         statement.close();

         System.out.println("Created tables successfully");
    }
}
