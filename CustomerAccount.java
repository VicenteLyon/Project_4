import java.util.ArrayList;

// Author: Chris Fietkiewicz
public class CustomerAccount extends Account {
    private String profile;

    public CustomerAccount(String username, String password, String profile, String id) {
        super(username, password, id); 
        this.profile = profile;
    }
    public String getProfile() {
        return profile;
    }
}
