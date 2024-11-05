import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class AccountsReader {
    public static HashMap<String, Account> readFile(String filename) {
        HashMap<String, Account> accountsMap = new HashMap<>();

        try {
            File xmlFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList accountList = doc.getElementsByTagName("account");
            for (int i = 0; i < accountList.getLength(); i++) {
                Element accountElement = (Element) accountList.item(i);
                
                String id = accountElement.getAttribute("id"); // Use ID for key in the HashMap
                String username = accountElement.getElementsByTagName("username").item(0).getTextContent();
                String password = accountElement.getElementsByTagName("password").item(0).getTextContent();
                String profile = accountElement.getElementsByTagName("profile").item(0).getTextContent();

                // Create an instance of the appropriate Account subclass
                Account account;
                if (accountElement.getElementsByTagName("type").item(0).getTextContent().equals("admin")) {
                    account = new AdminAccount(username, password, new ArrayList<>(), id); // Pass id
                } else {
                    account = new CustomerAccount(username, password, profile, id); // Pass id
                }

                accountsMap.put(id, account); // Store account using ID as the key
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accountsMap;
    }
}
