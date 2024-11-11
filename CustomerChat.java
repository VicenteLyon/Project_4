import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CustomerChat extends SceneBasic {
    private TextArea chatArea = new TextArea();
    private TextField messageField = new TextField();
    private Button sendButton = new Button("Send");

    public CustomerChat() {
        super("Customer Chat");
        VBox layout = new VBox(10);
        layout.getChildren().addAll(chatArea, messageField, sendButton);
        root.getChildren().add(layout);

        sendButton.setOnAction(e -> sendMessage());
    }

    private void sendMessage() {
        // Send the message to the server
        String message = messageField.getText();
        chatArea.appendText("You: " + message + "\n");
        messageField.clear();
    }
}
