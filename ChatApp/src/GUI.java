import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class GUI extends Application {
    private Client client = new Client();
    private TextArea chatArea = new TextArea();
    private TextField inputField = new TextField();
    private TextField usernameField = new TextField("User");
    private Button sendBtn = new Button("Send");
    private Button uploadBtn = new Button("File");
    private Button connectBtn = new Button("Join Group");

    public static void main(String[] args) { launch(args); }
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chat App");

        VBox loginLayout = new VBox(10, new Label("Username:"), usernameField, connectBtn);
        loginLayout.setPadding(new Insets(20));
        Scene loginScene = new Scene(loginLayout, 300, 150);

        BorderPane chatLayout = new BorderPane();
        chatLayout.setPadding(new Insets(10));
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatLayout.setCenter(chatArea);

        HBox inputBar = new HBox(10, inputField, sendBtn, uploadBtn);
        inputBar.setPadding(new Insets(10, 0, 0, 0));
        HBox.setHgrow(inputField, Priority.ALWAYS);
        chatLayout.setBottom(inputBar);
        Scene chatScene = new Scene(chatLayout, 500, 400);

        connectBtn.setOnAction(e -> {
            String user = usernameField.getText().trim();
            if (!user.isEmpty()) {
                try {
                    client.connect("localhost", 12345, user, this::handleIncomingMessage);
                    primaryStage.setScene(chatScene);
                } catch (IOException ex) { }
            }
        });

        sendBtn.setOnAction(e -> sendMessage());
        inputField.setOnAction(e -> sendMessage());
        uploadBtn.setOnAction(e -> {
            File file = new FileChooser().showOpenDialog(primaryStage);
            if (file != null) client.sendFileNotification(file.getName());
        });

        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private void sendMessage() {
        String text = inputField.getText().trim();
        if (!text.isEmpty()) {
            client.sendTextMessage(text);
            inputField.clear();
        }
    }

    private void handleIncomingMessage(String message) {
        String[] parts = message.split("\\|", 3);
        if (parts.length < 3) return;
        String type = parts[0], sender = parts[1], content = parts[2];

        Platform.runLater(() -> {
            String prefix = type.contains("HIST") ? "[History] " : "";
            if (type.contains("FILE")) {
                chatArea.appendText(prefix + "[" + sender + " shared a file]: " + content + "\n");
            } else {
                chatArea.appendText(prefix + sender + ": " + content + "\n");
            }
        });
    }

    public void stop() { 
        client.disconnect(); 
    }
}