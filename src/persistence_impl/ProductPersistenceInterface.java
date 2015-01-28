package persistence_impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import persistence.PersistenceInterface;
import model.Product;

/**
 * Implements the persistence layer for Product objects 
 */
public class ProductPersistenceInterface extends PersistenceInterface<Product> {
    /** Stores the persistence interface for a given Connection */ 
    private static Map<Connection, ProductPersistenceInterface> instances = new HashMap<Connection, ProductPersistenceInterface>();
    
    /**
     * Returns a persistence interface for a given Connection. If a persistence interface for a Connection
     * is already present, this persistence interface will be returned 
     */
    public static ProductPersistenceInterface getInstance(Connection conn) throws SQLException {
        if(instances.get(conn) == null) {
            instances.put(conn, new ProductPersistenceInterface(conn));
        }
        return instances.get(conn);
    }
    private PreparedStatement saveProductStmt;
    private PreparedStatement updateProductStmt;
    private PreparedStatement getProductStmt;
    private PreparedStatement allProductsStmt;
    
    public ProductPersistenceInterface(Connection conn) throws SQLException {
        // Prepare statements to insert, update and fetch Products
        saveProductStmt = conn.prepareStatement("INSERT INTO product(prod_nr, name, price) VALUES (?, ?, ?)");
        updateProductStmt = conn.prepareStatement("UPDATE product SET name = ?, price = ? WHERE prod_nr = ?");
        getProductStmt = conn.prepareStatement("SELECT prod_nr, name, price FROM product WHERE prod_nr = ?");
        allProductsStmt = conn.prepareStatement("SELECT prod_nr, name, price FROM product");
    }
    
    /**
     * Save a Product
     */
    @Override public void _save(Product product) throws SQLException {
        saveProductStmt.setInt(1, product.getProdNr());
        saveProductStmt.setString(2, product.getName());
        saveProductStmt.setInt(3, product.getPrice());
        saveProductStmt.executeUpdate();
    }
    
    /**
     * Update a Product
     */
    @Override public void _update(Product product) throws SQLException {
        updateProductStmt.setString(1, product.getName());
        updateProductStmt.setInt(2, product.getPrice());
        updateProductStmt.setInt(3, product.getProdNr());
        updateProductStmt.executeUpdate();
    }
    
    /**
     * Fetch a Product by it prod_nr
     */
    @Override public Product _fetch(Object key) throws SQLException {
        if(key instanceof Integer) {
            Integer prodNr = (Integer)key;
            getProductStmt.setInt(1, prodNr);
            ResultSet r = getProductStmt.executeQuery();
            if(r.next()) {
                return new Product(r.getInt(1), r.getString(2), r.getInt(3)); 
            } else {
                throw new RuntimeException("Product with product number: " + prodNr + " does not exist");
            }
        } else {
            throw new RuntimeException("Key for product must be of type Integer");
        }
    }
    
    /**
     * List all Products
     */
    @Override public List<Product> _all() throws SQLException {
        ArrayList<Product> products = new ArrayList<Product>();
        ResultSet r = allProductsStmt.executeQuery();
        while(r.next()) {
            products.add(new Product(r.getInt(1), r.getString(2), r.getInt(3)));
        }
        return products;
    }
    
    /**
     * Returns the key of a Product
     */
    @Override public Object key(Product product) {
        return product.getProdNr();
    }
}
