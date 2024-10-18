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

/**
 * Die ThreadFX Klasse stellt eine JavaFX-Anwendung dar, die es dem Benutzer ermöglicht,
 * eine Textdatei auszuwählen und deren Inhalt in einem neuen Fenster anzuzeigen. Sie verwendet
 * JavaFX's Service und Task, um das Lesen der Datei im Hintergrund durchzuführen.
 */
public class ThreadFX extends Application {

    private File selectedFile;

    /**
     * Startet die JavaFX-Anwendung, indem die Stages definiert werden, die für
     * die Dateiauswahl und das Einlesen der Datei verantwortlich sind.
     *
     * @param stage Hauptfenster der Anwendung
     * @throws Exception falls beim Initialisieren der Anwendung ein Fehler auftritt
     */
    @Override
    public void start(Stage stage) throws Exception {

        final VBox content = new VBox();
        content.setPrefSize(400, 150);

        final Button button = new Button("Datei ausw\u00e4hlen");

        final Label label = new Label();

        content.getChildren().addAll(button, label);
        stage.setScene(new Scene(content));
        stage.show();

        // Erstellt einen Service, um das Einlesen der Datei im Hintergrund durchzuführen
        final Service<String> service = new Service<String>() {

            /**
             * Erstellt die Aufgabe, um den Inhalt der Datei einzulesen
             *
             * @return Task, die den Inhalt der ausgewählten Datei einliest
             */
            @Override
            protected Task<String> createTask() {
                return new Task<String>() {
                    @Override
                    protected String call() throws Exception {
                        if (selectedFile != null) {
                            return new String(Files.readAllBytes(selectedFile.toPath()));
                        }
                        return null;
                    }
                };
            }
        };

        // Setzt Event-Handler, der nach erfolgreichem Abschluss der Aufgabe ausgeführt wird
        service.setOnSucceeded(event -> {
            String fileContent = service.getValue();
            if (fileContent != null) {
                Platform.runLater(() -> {
                    final Label labelZwei = new Label("Datei erfolgreich gelesen:\n" + fileContent);

                    // Erstellt ein neues Fenster, um den Dateiinhalt anzuzeigen
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

        // Definiert die Aktion, die ausgeführt wird, wenn der Button gedrückt wird
        button.setOnAction(event -> {
            locateFile(event);
            if (selectedFile != null) {
                System.out.println("Datei wurde ausgew\u00e4hlt: " + selectedFile.getName());
                service.reset();
                service.start();  // Startet Service zum Einlesen der Datei
            }
        });
    }

    /**
     * Öffnet File Chooser, um Datei auszuwählen
     * @param event ActionEvent, das durch Button-Klick ausgelöst wird
     */
    protected void locateFile(ActionEvent event) {
        System.out.println("Button gedrückt, locateFile aufgerufen!");
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Datei \u00f6ffnen");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Textdateien", "*.txt"));
        selectedFile = chooser.showOpenDialog(new Stage());
    }
}
