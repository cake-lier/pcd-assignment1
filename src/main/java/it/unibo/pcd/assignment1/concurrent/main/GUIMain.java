package it.unibo.pcd.assignment1.concurrent.main;

import it.unibo.pcd.assignment1.concurrent.view.impl.GUIView;
import javafx.application.Application;
import javafx.stage.Stage;


public class GUIMain extends Application {
    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        new GUIView(primaryStage);
    }
}
