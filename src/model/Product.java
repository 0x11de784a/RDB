package model;

/**
 * Simple plain Product class 
 */
public class Product {
    private int prodNr;
    private String name;
    private int price;
    
    public Product(int prodNr, String name, int price) {
        this.prodNr = prodNr;
        this.name = name;
        this.price = price;
    }
    
    public int getProdNr() {
        return prodNr;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getPrice() {
        return price;
    }
    
    public void setPrice(int price) {
        this.price = price;
    }
    
    public String toString() {
        return "Product(" + prodNr + ", " + name + ", " + price/100 + "." + ((price%100<10) ? "0" : "") + price%100 + "€)";
    }
}
