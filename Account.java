// Author: Chris Fietkiewicz
public abstract class Account {
    private String username;
    private String password;
    private String id; 

    public Account(String username, String password, String id) {
        this.username = username;
        this.password = password;
        this.id = id; 
    }

    public String getUsername() {
        return username;
    }

    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }
    
    @Override
    public String toString() {
        return "username: " + username + ", id: " + id + ", " + this.getClass();
    }
}
