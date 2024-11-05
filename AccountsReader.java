import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

public class AccountsReader {
    public List<Account> readAccounts(String filePath) {
        List<Account> accounts = new ArrayList<>();

        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList accountList = doc.getElementsByTagName("account");
            
            for (int i = 0; i < accountList.getLength(); i++) {
                Node accountNode = accountList.item(i);

                if (accountNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element accountElement = (Element) accountNode;

                    int id = Integer.parseInt(accountElement.getElementsByTagName("id").item(0).getTextContent());
                    String username = accountElement.getElementsByTagName("username").item(0).getTextContent();
                    String password = accountElement.getElementsByTagName("password").item(0).getTextContent();
                    String profile = accountElement.getElementsByTagName("profile").item(0).getTextContent();
                    String type = accountElement.getElementsByTagName("type").item(0).getTextContent();

                    if ("Customer".equalsIgnoreCase(type)) {
                        accounts.add(new CustomerAccount(username, password, profile, id));
                    } else if ("Admin".equalsIgnoreCase(type)) {
                        ArrayList<Account> adminAccounts = new ArrayList<>();
                        NodeList adminAccountIds = accountElement.getElementsByTagName("accountID");
                        for (int j = 0; j < adminAccountIds.getLength(); j++) {
                            int adminId = Integer.parseInt(adminAccountIds.item(j).getTextContent());
                            Account adminAccount = getAccountById(accounts, adminId);
                            if (adminAccount != null) {
                                adminAccounts.add(adminAccount);
                            }
                        }
                        accounts.add(new AdminAccount(username, password, adminAccounts, id));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accounts;
    }

    private Account getAccountById(List<Account> accounts, int id) {
        for (Account account : accounts) {
            if (account.getId() == id) {
                return account;
            }
        }
        return null; 
    }
}
