package main;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import persistence.DBHelper;
import persistence_impl.CustomerPersistenceInterface;
import persistence_impl.ProductPersistenceInterface;
import persistence_impl.ShoppingBasketPersistenceInterface;
import model.Customer;
import model.Product;
import model.ShoppingBasket;

public class Main {
    public static void main(String[] args) {
        Connection conn = null;
        try {
            // == Preparation
            // uncomment and adept the following line, if you want to clear your database
            new File("test.db").delete();

            conn = DBHelper.getConnection();
            
            // == Task 1: Create schema
            DBHelper.createSchema(conn);
            
            // == Task 2: Create Customers and Products
            createCustomersAndProducts(conn);
            
            // == Task 3: Update Customer and Product
            updateEntities(conn);
            
            // == Task 4: Put Products into ShoppingBasket
            fillShoppingBasket(conn);
            
            // == Task 5: Remove Product from a ShoppingBasket
            updateShoppingBasket(conn);
            
            // == Task 6: Trying to remove a Product from a ShoppingBasket by using the wrong methods
            wrongUpdateShoppingBasket(conn);
            
            // == Task 7: Show all Customers, Products and ShoppingBaskets
            showData(conn);
            
            /*
             * In the end, the output of showData(conn) should be:
             * Customers: [Customer(bob@net.de, Robert, 1234), Customer(jill@uk.net, Jill, abcd), Customer(matthew@it.com, Matthew, password)]
             * Products: [Product(1, Lange Hose, 11.00€), Product(2, Karierte Tasse, 6.50€), Product(3, Grosser Eimer, 8.30€)]
             * ShoppingBaskets: [ShoppingBasket(1, Customer(bob@net.de, Robert, 1234), [1x Lange Hose, 2x Karierte Tasse])]
             */
            
            conn.close();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Creates some Product and Customer objects and stores them in the database  
     */
    public static void createCustomersAndProducts(Connection conn) throws SQLException {
        // Persistence interfaces to save, fetch and update entities
        CustomerPersistenceInterface cpi = CustomerPersistenceInterface.getInstance(conn);
        ProductPersistenceInterface ppi = ProductPersistenceInterface.getInstance(conn);
        
        // create some flat customer objects
        Customer bob = new Customer("bob@net.de", "Bob", "1234");
        Customer jill = new Customer("jill@uk.net", "Jill", "abcd");
        Customer matthew = new Customer("matthew@it.com", "Matthew", "password");
        // save the customers persistently using the persistence interface
        cpi.save(bob);
        cpi.save(jill);
        cpi.save(matthew);
        
        // create some flat product objects
        Product langeHose = new Product(1, "Lange Hose", 2000);
        Product karierteTasse = new Product(2, "Karierte Tasse", 650);
        Product grosserEimer = new Product(3, "Grosser Eimer", 830);  
        // save products persistently
        ppi.save(langeHose);
        ppi.save(karierteTasse);
        ppi.save(grosserEimer);
    }
    
    /**
     * Executes some updates on entities stored in the database
     */
    public static void updateEntities(Connection conn) throws SQLException {
        // Persistence interfaces to save, fetch and update entities
        CustomerPersistenceInterface cpi = CustomerPersistenceInterface.getInstance(conn);
        ProductPersistenceInterface ppi = ProductPersistenceInterface.getInstance(conn);
        
        // Update name of Bob
        Customer bob = cpi.fetch("bob@net.de");
        bob.setName("Robert");
        cpi.update(bob);
        
        // Update price of "Lange Hose" to 23,50€
        Product langeHose = ppi.fetch(1);
        langeHose.setPrice(2350);
        ppi.update(langeHose);
    }
    
    /**
     * Creates a ShoppingBasket, puts some Products into it and stores it in the database
     */
    public static void fillShoppingBasket(Connection conn) throws SQLException {
        // Persistence interfaces to save, fetch and update entities
        CustomerPersistenceInterface cpi = CustomerPersistenceInterface.getInstance(conn);
        ProductPersistenceInterface ppi = ProductPersistenceInterface.getInstance(conn);
        ShoppingBasketPersistenceInterface sbpi = ShoppingBasketPersistenceInterface.getInstance(conn);
        
        // Fetch some stored Products from the database
        Customer bob = cpi.fetch("bob@net.de");
        Product langeHose = ppi.fetch(1);
        Product karierteTasse = ppi.fetch(2);
        Product grosserEimer = ppi.fetch(3);
        
        // Put some Products into the ShoppingBasket
        ShoppingBasket sb = new ShoppingBasket(1, bob);
        sb.addProduct(langeHose, 1);
        sb.addProduct(karierteTasse, 2);
        sb.addProduct(grosserEimer, 5);
        
        // Store the ShoppingBasket in the database
        sbpi.save(sb);
    }
    
    /**
     * Fetches the created ShoppingBasket and removes a product
     */
    public static void updateShoppingBasket(Connection conn) throws SQLException {
        // Persistence interfaces to save, fetch and update entities
        ProductPersistenceInterface ppi = ProductPersistenceInterface.getInstance(conn);
        ShoppingBasketPersistenceInterface sbpi = ShoppingBasketPersistenceInterface.getInstance(conn);
        
        Product grosserEimer = ppi.fetch(3);
        ShoppingBasket sb = sbpi.fetch(1);
        sb.removeProduct(grosserEimer);
        sbpi.update(sb);
    }
    
    /**
     * Tries to remove a Product from a ShoppingBasket with the wrong methods
     */
    public static void wrongUpdateShoppingBasket(Connection conn) throws SQLException {
        // Persistence interfaces to save, fetch and update entities
        ProductPersistenceInterface ppi = ProductPersistenceInterface.getInstance(conn);
        ShoppingBasketPersistenceInterface sbpi = ShoppingBasketPersistenceInterface.getInstance(conn);
        
        Product karierteTasse = ppi._fetch(2);
        ShoppingBasket sb = sbpi._fetch(1);
        sb.removeProduct(karierteTasse);
        sbpi._update(sb);
    }
    
    public static void showData(Connection conn) throws SQLException {
        // Persistence interfaces to save, fetch and update entities
        CustomerPersistenceInterface cpi = CustomerPersistenceInterface.getInstance(conn);
        ProductPersistenceInterface ppi = ProductPersistenceInterface.getInstance(conn);
        ShoppingBasketPersistenceInterface sbpi = ShoppingBasketPersistenceInterface.getInstance(conn);
        
        System.out.println("Customers: " + cpi.all());
        System.out.println("Products: " + ppi.all());
        System.out.println("ShoppingBaskets: " + sbpi.all());
    }
}
