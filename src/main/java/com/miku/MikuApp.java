
package com.miku;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class MikuApp extends Application {

    private final ObjectMapper mapper = new ObjectMapper();

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Override
    public void start(Stage stage) {

        Label title = new Label("Miku 💙");
        title.setStyle(
                "-fx-font-size: 30px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #8be9fd;"
        );

        Label message = new Label(
                "Hey! I'm Miku. Your AI companion is ready to chat."
        );
        message.setWrapText(true);
        message.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        TextField input = new TextField();
        input.setPromptText("Say something to Miku...");
        input.setMaxWidth(350);

        Button sendButton = new Button("Send 💙");

        Label reply = new Label("Your best friend is listening! ✨");
        reply.setWrapText(true);
        reply.setMaxWidth(430);
        reply.setStyle("-fx-text-fill: #b8c7d9; -fx-font-size: 14px;");

        sendButton.setOnAction(event -> {
            String text = input.getText().trim();

            if (text.isEmpty()) {
                return;
            }

            input.clear();
            input.setDisable(true);
            sendButton.setDisable(true);
            reply.setText("Miku is thinking... 💭");

            Thread aiThread = new Thread(() -> {
                String answer = askOllama(text);

                Platform.runLater(() -> {
                    reply.setText(answer);
                    input.setDisable(false);
                    sendButton.setDisable(false);
                    input.requestFocus();
                });
            });

            aiThread.setDaemon(true);
            aiThread.start();
        });

        input.setOnAction(event -> sendButton.fire());

        VBox layout = new VBox(
                18, title, message, input, sendButton, reply
        );

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #151923;");

        Scene scene = new Scene(layout, 520, 420);

        stage.setTitle("Miku - Your AI Companion");
        stage.setScene(scene);
        stage.setMinWidth(420);
        stage.setMinHeight(320);
        stage.show();
    }

    private String askOllama(String userMessage) {

        try {
            String requestBody = mapper.createObjectNode()
                    .put("model", "llama3.2:3b")
                    .put("prompt",
                            "You are Miku, the user's funny, loyal, warm AI best friend. "
                                    + "Be natural, playful, supportive, and conversational. "
                                    + "Keep replies reasonably concise. "
                                    + "You are running locally on the user's PC. "
                                    + "User says: " + userMessage)
                    .put("stream", false)
                    .toString();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:11434/api/generate"))
                    .timeout(Duration.ofSeconds(120))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() != 200) {
                return "Hmm, Ollama returned error "
                        + response.statusCode()
                        + ". Check that Ollama is running.";
            }

            JsonNode json = mapper.readTree(response.body());
            JsonNode responseNode = json.get("response");

            if (responseNode == null || responseNode.isNull()) {
                return "I received an unexpected response. Let's try again!";
            }

            return responseNode.asText();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "I got interrupted! Try sending that again. 💙";

        } catch (Exception e) {
            return "I couldn't reach my AI brain. Make sure Ollama is running. "
                    + "Details: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
