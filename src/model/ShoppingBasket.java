package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Simple plain ShoppingBasket class 
 */
public class ShoppingBasket {
    private int id;
    private Customer owner;
    // products in the shopping basket. The integer represents the number of times the product is in the ShoppingBasket
    private Map<Product, Integer> products;
    
    public ShoppingBasket(int id, Customer owner, Map<Product, Integer> products) {
        this.id = id;
        this.owner = owner;
        this.products = products;
    }
    
    public ShoppingBasket(int id, Customer owner) {
        this(id, owner, new HashMap<Product, Integer>());
    }
    
    public int getId() {
        return id;
    }
    
    public Customer getOwner() {
        return owner;
    }
    
    public void setOwner(Customer owner) {
        this.owner = owner;
    }
    
    public Map<Product, Integer> getProducts() {
        return products;
    }
    
    public void addProduct(Product product, int number) {
        products.put(product, number);
    }
    
    public void removeProduct(Product product) {
        products.remove(product);
    }
    
    public void clear() {
        products.clear();
    }
    
    public String toString() {
        StringBuffer productList = new StringBuffer();
        productList.append("[");
        
        Iterator<Map.Entry<Product, Integer>> iter = products.entrySet().iterator();
        if(iter.hasNext()) {
            Map.Entry<Product, Integer> entry = iter.next();
            productList.append(entry.getValue() + "x " + entry.getKey().getName());
        }
        
        while(iter.hasNext()) {
            productList.append(", ");
            Map.Entry<Product, Integer> entry = iter.next();
            productList.append(entry.getValue() + "x " + entry.getKey().getName());
        }
        productList.append("]");
        
        return "ShoppingBasket(" + id + ", " + owner + ", " + productList.toString() + ")";
    }
}
