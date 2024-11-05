import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

public class InventoryReader {
    public List<InventoryItem> readInventory(String filePath) {
        List<InventoryItem> inventoryItems = new ArrayList<>();

        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList itemList = doc.getElementsByTagName("item");

            for (int i = 0; i < itemList.getLength(); i++) {
                Node itemNode = itemList.item(i);

                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element itemElement = (Element) itemNode;

                    int id = Integer.parseInt(itemElement.getElementsByTagName("id").item(0).getTextContent());
                    String name = itemElement.getElementsByTagName("name").item(0).getTextContent();
                    String description = itemElement.getElementsByTagName("description").item(0).getTextContent();
                    int quantity = Integer.parseInt(itemElement.getElementsByTagName("quantity").item(0).getTextContent());
                    double price = Double.parseDouble(itemElement.getElementsByTagName("price").item(0).getTextContent());

                    inventoryItems.add(new InventoryItem(id, name, description, quantity, price));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return inventoryItems;
    }
}
