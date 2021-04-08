package it.unibo.pcd.assignment1.concurrent.main;

import it.unibo.pcd.assignment1.concurrent.view.impl.GUIView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main class for the application with a graphical user interface for the user.
 */
public class GUIMain extends Application {
    /**
     * The main method.
     * @param args unused
     */
    public static void main(final String[] args) {
        launch(args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(final Stage primaryStage) {
        new GUIView(primaryStage);
    }
}
