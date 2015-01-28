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
import model.Customer;

/**
 * Implements the persistence layer for Customer objects 
 */
public class CustomerPersistenceInterface extends PersistenceInterface<Customer> {
    /** Stores the persistence interface for a given Connection */ 
    private static Map<Connection, CustomerPersistenceInterface> instances = new HashMap<Connection, CustomerPersistenceInterface>();
    
    /**
     * Returns a persistence interface for a given Connection. If a persistence interface for a Connection
     * is already present, this persistence interface will be returned 
     */
    public static CustomerPersistenceInterface getInstance(Connection conn) throws SQLException {
        if(instances.get(conn) == null) {
            instances.put(conn, new CustomerPersistenceInterface(conn));
        }
        return instances.get(conn);
    }
    
    private PreparedStatement saveCustomerStmt;
    private PreparedStatement updateCustomerStmt;
    private PreparedStatement getCustomerStmt;
    private PreparedStatement allCustomersStmt;
    
    public CustomerPersistenceInterface(Connection conn) throws SQLException {
        // Prepare statements to insert, update and fetch Customers
        saveCustomerStmt = conn.prepareStatement("INSERT INTO customer(email, name, password) VALUES (?, ?, ?)");
        updateCustomerStmt = conn.prepareStatement("UPDATE customer SET name = ?, password = ? WHERE email = ?");
        getCustomerStmt = conn.prepareStatement("SELECT email, name, password FROM customer WHERE email = ?");
        allCustomersStmt = conn.prepareStatement("SELECT email, name, password FROM customer");
    }
    
    /**
     * Save a Customer
     */
    @Override public void _save(Customer customer) throws SQLException {
        saveCustomerStmt.setString(1, customer.getEmail());
        saveCustomerStmt.setString(2, customer.getName());
        saveCustomerStmt.setString(3, customer.getPassword());
        saveCustomerStmt.executeUpdate();
    }
    
    /**
     * Update a Customer
     */
    @Override public void _update(Customer customer) throws SQLException {
        updateCustomerStmt.setString(1, customer.getName());
        updateCustomerStmt.setString(2, customer.getPassword());
        updateCustomerStmt.setString(3, customer.getEmail());
        updateCustomerStmt.executeUpdate();
    }
    
    /**
     * Fetch a Customer by its email address
     */
    @Override public Customer _fetch(Object key) throws SQLException {
        if(key instanceof String) {
            String email = (String)key;
            getCustomerStmt.setString(1, email);
            ResultSet r = getCustomerStmt.executeQuery();
            if(r.next()) {
                return new Customer(r.getString(1), r.getString(2), r.getString(3)); 
            } else {
                throw new RuntimeException("Customer with email: " + email + " does not exist");
            }
        } else {
            throw new RuntimeException("Key for customer must be of type String");
        }
    }
    
    /**
     * List all Customers
     */
    @Override public List<Customer> _all() throws SQLException {
        ArrayList<Customer> customers = new ArrayList<Customer>();
        ResultSet r = allCustomersStmt.executeQuery();
        while(r.next()) {
            customers.add(new Customer(r.getString(1), r.getString(2), r.getString(3)));
        }
        return customers;
    }
    
    /**
     * Returns the key of a Customer
     */
    @Override public Object key(Customer customer) {
        return customer.getEmail();
    }
}
