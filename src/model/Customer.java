package model;

/**
 * Simple plain Customer class 
 */
public class Customer {
    private String email;
    private String name;
    private String password;
    
    public Customer(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return "Customer(" + email + ", " + name + ", " + password + ")";
    }
}
