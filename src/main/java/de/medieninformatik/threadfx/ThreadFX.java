package de.medieninformatik.threadfx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ThreadFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        final VBox content = new VBox();
        content.setPrefSize(400, 150);

        final Button button = new Button("Choose File");

        
        //TODO
        //Progress Bar wird für die Aufgabe nicht benötigt
        final ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(350);
        progressBar.progressProperty().setValue(0.0);

        final Label label = new Label();

        content.getChildren().addAll(button, progressBar, label);
        stage.setScene(new Scene(content));
        stage.show();

        final Service<Void> service = new Service<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //TODO
                        //Hier muss vernünftige "Arbeit" eingefügt werden

                        final int MEASURE_WORK = 5000;
                        final int STEP = MEASURE_WORK / 20;
                        int work = 0;

                        while (work < MEASURE_WORK) {
                            work += STEP;
                            //warten anstatt "echter" Aufgabe
                            Thread.sleep(STEP);
                            // melde Fortschritt
                            updateProgress(work, MEASURE_WORK);
                        }
                        Platform.runLater(
                                () -> label.textProperty().setValue("Done")
                        );
                        return null;
                    }
                };
            }
        };
        /*button.setOnAction(event -> {
            service.reset();
            DoubleProperty progressProp = progressBar.progressProperty();
            if (progressProp.isBound()) progressProp.unbind();
            progressProp.bind(service.progressProperty());
            label.textProperty().setValue("Warte ....");
            service.start();
        });

         */

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                locateFile(event);

            }
        });



        //TODO
        //recherchiert, wie man Code ausführen kann, direkt nachdem ein Service
        //erfolgreich abgeschlossen ist
    }

    protected void locateFile(ActionEvent event) {
        System.out.println("Button gedrückt, locateFile aufgerufen!");
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        File file = chooser.showOpenDialog(new Stage());
        file.showDocument(file.toURI().toString());
    }
}

