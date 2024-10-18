package de.medieninformatik.threadfx;

import javafx.application.Application;

/**
 * @author Luk Weiser m30971
 * @author Tabea Sudrow m30902
 * date: 14-10-2024
 * Programmierung 3, Aufgabe 4
 * Beschreibung der Klasse:
 * In der Main-Klasse wird die JavaFX-Anwendung gestartet
 */
public class Main {

    /**
     * Durch den Aufruf der launch-Methode von Application wird
     * die start Methode aufgerufen und die Anwendung initialisiert
     * @param args die Argumente der Kommandozeile
     */
    public static void main(String[] args) {
        Application.launch(ThreadFX.class, args);
    }
}
