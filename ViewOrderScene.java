import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

public class ViewOrderScene extends SceneBasic {
    private ListView<String> orderListView = new ListView<>();

    public ViewOrderScene() {
        super("View Orders");
        orderListView.setItems(FXCollections.observableArrayList("Order 1", "Order 2"));
        root.getChildren().add(orderListView);
    }
}
