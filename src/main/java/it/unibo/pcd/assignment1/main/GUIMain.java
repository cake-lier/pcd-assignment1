package it.unibo.pcd.assignment1.main;

import it.unibo.pcd.assignment1.view.gui.GUIView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GUIMain extends Application {
    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        new GUIView(primaryStage);
    }
}
