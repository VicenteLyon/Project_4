import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import java.net.Socket;

public class SettingsScene extends SceneBasic {
    private Label hostText = new Label("Host");
    private TextField hostField = new TextField("localhost");
    private Label portText = new Label("Port");
    private TextField portField = new TextField("32007"); // Default port for testing
    private Button applyButton = new Button("Apply");
    private Button cancelButton = new Button("Cancel");
    private Button chatButton = new Button("Chat"); // New Chat button
    private Label errorMessage = new Label();

    public SettingsScene() {
        super("Connection Settings");

        // Creating Grid Pane for layout
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(400, 200);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.add(hostText, 0, 0);
        gridPane.add(hostField, 1, 0);
        gridPane.add(portText, 0, 1);
        gridPane.add(portField, 1, 1);

        // Button layout
        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(applyButton, cancelButton, chatButton); // Adding Chat button
        gridPane.add(buttonBox, 1, 2);

        // Error message
        errorMessage.setTextFill(Color.RED);
        gridPane.add(errorMessage, 1, 3);
        gridPane.setAlignment(Pos.TOP_CENTER);
        root.getChildren().addAll(gridPane);

        // Set button actions
        applyButton.setOnAction(e -> apply());
        cancelButton.setOnAction(e -> cancel());
        chatButton.setOnAction(e -> startChat()); // Start chat action
    }

    // Apply settings and attempt a connection
    private void apply() {
        String host = hostField.getText();
        String port = portField.getText();
        try {
            Socket connection = new Socket(host, Integer.parseInt(port));
            SceneManager.setSocket(connection); // Set client socket
            System.out.println("Connection = " + connection); // For debugging
            errorMessage.setText(""); // Clear previous errors
            SceneManager.setLoginScene();
        } catch (Exception e) {
            errorMessage.setText("Error trying to connect to server.");
            System.out.println("Error:  " + e);
        }
    }

    // Cancel settings change and return to login scene
    private void cancel() {
        errorMessage.setText(""); // Clear any previous error messages
        SceneManager.setLoginScene();
    }

    // Start customer chat in a new window
    private void startChat() {
        CustomerChat chat = new CustomerChat();
        chat.start(new Stage());
    }
}
