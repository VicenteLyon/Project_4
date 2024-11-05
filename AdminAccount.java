import java.util.ArrayList;

// Author: Chris Fietkiewicz
public class AdminAccount extends Account {
    private ArrayList<Account> accounts;

    public AdminAccount(String username, String password, ArrayList<Account> accounts, String id) {
        super(username, password, id); // Pass id to the superclass
        this.accounts = accounts;
    }
}