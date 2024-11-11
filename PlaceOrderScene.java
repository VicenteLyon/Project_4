import javafx.scene.control.Button;

public class PlaceOrderScene extends SceneBasic {
    private Button placeOrderButton = new Button("Place Order");

    public PlaceOrderScene() {
        super("Place Order");
        placeOrderButton.setOnAction(e -> placeOrder());
        root.getChildren().add(placeOrderButton);
    }

    private void placeOrder() {
        // Logic to place an order
    }
}
