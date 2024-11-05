// Author: Chris Fietkiewicz
public class InventoryItem {
    private int id;
    private String name;
    private String description;
    private int quantity;
    private double price;

    public InventoryItem(int id, String name, String description, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters for inventory item properties
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Description: " + description +
               ", Quantity: " + quantity + ", Price: $" + price;
    }
}
