package persistence_impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Customer;
import model.Product;
import persistence.PersistenceInterface;
import model.ShoppingBasket;

/**
 * Implements the persistence layer for ShoppingBasket objects 
 */
public class ShoppingBasketPersistenceInterface extends PersistenceInterface<ShoppingBasket> {
    /** Stores the persistence interface for a given Connection */ 
    private static Map<Connection, ShoppingBasketPersistenceInterface> instances = new HashMap<Connection, ShoppingBasketPersistenceInterface>();
    
    /**
     * Returns a persistence interface for a given Connection. If a persistence interface for a Connection
     * is already present, this persistence interface will be returned 
     */
    public static ShoppingBasketPersistenceInterface getInstance(Connection conn) throws SQLException {
        if(instances.get(conn) == null) {
            instances.put(conn, new ShoppingBasketPersistenceInterface(conn));
        }
        return instances.get(conn);
    }
    
    private Connection conn;
    
    private PreparedStatement saveShoppingBasketStmt;
    private PreparedStatement updateShoppingBasket;
    private PreparedStatement getShoppingBasketStmt;
    private PreparedStatement allShoppingBasketsStmt;
    
    private PreparedStatement saveProductInShoppingBasketStmt;
    private PreparedStatement deleteProductsInShoppingBasketStmt;
    private PreparedStatement getProductsInShoppingBasketStmt;
    
    public ShoppingBasketPersistenceInterface(Connection conn) throws SQLException {
        this.conn = conn;
        
        // Prepare statements to insert, update and fetch ShoppingBaskets
        // saveShoppingBasketStmt = <Prepared statement to store a shopping basket>;
        saveShoppingBasketStmt = conn.prepareStatement("INSERT INTO shopping_basket(id, customer) VALUES (?, ?)");
        // updateShoppingBasket = <Prepared statement to update a shopping basket>;
        updateShoppingBasket = conn.prepareStatement("UPDATE shopping_basket SET customer = ? WHERE id = ?");
        // getShoppingBasketStmt = <Prepared statement to fetch a shopping basket>;
        getShoppingBasketStmt = conn.prepareStatement("SELECT id, customer FROM shopping_basket WHERE id = ?");

        // allShoppingBasketsStmt = <Prepared statement to fetch all shopping baskets>;
        allShoppingBasketsStmt = conn.prepareStatement("SELECT id, customer FROM shopping_basket");
        
        // saveProductInShoppingBasketStmt = <Prepared statement to store a "product in shopping basket"-tuple>;
        saveProductInShoppingBasketStmt = conn.prepareStatement("INSERT INTO product_in_shopping_basket(" +
                                                                "product, shopping_basket, number)" +
                                                                " VALUES (?, ?, ?)");
        // deleteProductsInShoppingBasketStmt = <Prepared statement to remove a "product in shopping basket"-tuple>;
        deleteProductsInShoppingBasketStmt = conn.prepareStatement("DELETE FROM product_in_shopping_basket WHERE " +
                                                                    "product = ? AND shopping_basket = ?");
        // getProductsInShoppingBasketStmt = <Prepared statement to get all "product in shopping basket"-tuples for a given shopping basket>;
        getProductsInShoppingBasketStmt = conn.prepareStatement("SELECT * FROM product_in_shopping_basket" +
                                                                " WHERE shopping_basket = ?");
    }
    
    /**
     * Save a ShoppingBasket
     */
    @Override public void _save(ShoppingBasket shoppingBasket) throws SQLException {
        //throw new RuntimeException("Please implement this method!");
        saveShoppingBasketStmt.setInt(1, shoppingBasket.getId());
        saveShoppingBasketStmt.setString(2, shoppingBasket.getOwner().getEmail());
        saveShoppingBasketStmt.executeUpdate();
    }
    
    /**
     * Update a ShoppingBasket
     */
    @Override public void _update(ShoppingBasket shoppingBasket) throws SQLException {
        //throw new RuntimeException("Please implement this method!");
        updateShoppingBasket.setString(1, shoppingBasket.getOwner().getEmail());
        updateShoppingBasket.setInt(2, shoppingBasket.getId());
        updateShoppingBasket.executeUpdate();
    }
    
    /**
     * Fetch a ShoppingBasket by its ID
     */
    @Override public ShoppingBasket _fetch(Object key) throws SQLException {
        // == TODO: implement this method
        //throw new RuntimeException("Please implement this method!");
        // result set
        if(key instanceof Integer) {
            Integer shoppingBasketId = (Integer)key;
            getShoppingBasketStmt.setInt(1, shoppingBasketId);
            ResultSet r = getShoppingBasketStmt.executeQuery();
            if(r.next()) {
                //workaround -
                CustomerPersistenceInterface cpi = CustomerPersistenceInterface.getInstance(conn);
                Customer customer = cpi.fetch(r.getString(2));

                //getting products for that certain basket
                Map<Product, Integer> products = new HashMap<Product, Integer>();   //int = anzahl
                getProductsInShoppingBasketStmt.setInt(1, shoppingBasketId);
                ResultSet rs = getProductsInShoppingBasketStmt.executeQuery();
                //if there are no products in basket? is just a empty map alright?
                while(rs.next()) {
                    Product product = ProductPersistenceInterface.getInstance(conn).fetch(rs.getInt(1));
                    products.put(product, rs.getInt(2));
                }

                //return new ShoppingBasket(shoppingBasketId, customer);
                return new ShoppingBasket(shoppingBasketId, customer, products);
            } else {
                throw new RuntimeException("Shopping basket with ID: " + shoppingBasketId + " does not exist");
            }
        } else {
            throw new RuntimeException("Key for shopping basket must be of type Integer");
        }
    }
    
    /**
     * Fetch all ShoppingBaskets
     */
    @Override public List<ShoppingBasket> _all() throws SQLException {
        // == TODO: implement this method
        //throw new RuntimeException("Please implement this method!");

        ArrayList<ShoppingBasket> shoppingBaskets = new ArrayList<ShoppingBasket>();
        ResultSet r = allShoppingBasketsStmt.executeQuery();
        CustomerPersistenceInterface cpi = CustomerPersistenceInterface.getInstance(conn);
        while(r.next()) {
            Customer customer = cpi.fetch(r.getString(2));

            //getting products for that certain basket
            Map<Product, Integer> products = new HashMap<Product, Integer>();   //int = anzahl
            getProductsInShoppingBasketStmt.setInt(1, r.getInt(1));
            ResultSet rs = getProductsInShoppingBasketStmt.executeQuery();
            //if there are no products in basket? is just a empty map alright?
            while(rs.next()) {
                Product product = ProductPersistenceInterface.getInstance(conn).fetch(rs.getInt(1));
                products.put(product, rs.getInt(3));
            }

            shoppingBaskets.add(new ShoppingBasket(r.getInt(1), customer, products));
        }
        return shoppingBaskets;
    }
    
    /**
     * Returns the key of a ShoppingBasket
     */
    @Override public Object key(ShoppingBasket shoppingBasket) {
        return shoppingBasket.getId();
    }
}
