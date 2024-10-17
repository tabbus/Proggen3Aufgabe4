package de.medieninformatik.threadfx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;

public class ThreadFX extends Application {

    private File selectedFile;

    @Override
    public void start(Stage stage) throws Exception {

        final VBox content = new VBox();
        content.setPrefSize(400, 150);

        final Button button = new Button("Choose File");
        final ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(350);
        progressBar.progressProperty().setValue(0.0);

        final Label label = new Label();

        content.getChildren().addAll(button, progressBar, label);
        stage.setScene(new Scene(content));
        stage.show();

        final Service<String> service = new Service<String>() {

            @Override
            protected Task<String> createTask() {
                return new Task<String>() {
                    @Override
                    protected String call() throws Exception {
                        if (selectedFile != null) {
                            // Dateiinhalt lesen
                            return new String(Files.readAllBytes(selectedFile.toPath()));
                        }
                        return null;
                    }
                };
            }
        };

        // Setze den onSucceeded Event-Handler
        service.setOnSucceeded(event -> {
            // Das Ergebnis der Aufgabe (der Inhalt der Datei)
            String fileContent = service.getValue();
            if (fileContent != null) {
                Platform.runLater(() -> {
                    final Label labelZwei = new Label("Datei erfolgreich gelesen:\n" + fileContent);

                    Stage stageZwei = new Stage();
                    final VBox contentZwei = new VBox();
                    Scene sceneZwei = new Scene(contentZwei);
                    contentZwei.getChildren().addAll(labelZwei);
                    contentZwei.setPrefSize(400, 300);

                    stageZwei.setScene(sceneZwei);
                    stageZwei.show();
                });
            }
        });

        button.setOnAction(event -> {
            locateFile(event);
            if (selectedFile != null) {
                System.out.println("Datei wurde ausgewählt: " + selectedFile.getName());
                service.reset();
                service.start();
            }
        });
    }

    protected void locateFile(ActionEvent event) {
        System.out.println("Button gedrückt, locateFile aufgerufen!");
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        selectedFile = chooser.showOpenDialog(new Stage());
    }
}