package it.unibo.pcd.assignment1.view.gui;

import it.unibo.pcd.assignment1.controller.Controller;
import it.unibo.pcd.assignment1.controller.impl.ControllerImpl;
import it.unibo.pcd.assignment1.view.View;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class GUIView implements View {
    public static final String STOPWORDS_FILE_ERROR_MSG = "Select a file containing the stopwords";
    public static final String PDFS_FOLDER_ERROR_MSG = "Select a folder for your PDF files";
    public static final String FXML_FILE_ERROR_MSG = "An error occured while loading configuration files: %s";
    public static final String APP_TITLE = "Unique words counter";
    public static final String FXML_FILENAME = "main.fxml";
    public static final String PROCESSED_WORDS_LABEL_MSG = "Processed words: %d";
    public static final String PDFS_FOLDER_CHOOSER_TITLE = "Choose directory with PDFs files";
    public static final String INITIAL_DIRECTORY = System.getProperty("user.home");
    public static final String STOPWORDS_FILE_CHOOSER_TITLE = "Choose stopwords file";
    public static final String FILE_CHOOSER_DESC = "Text files";
    public static final String FILE_CHOOSER_TXT_EXT = "*.txt";
    public static final String FILE_NOT_CHOSEN_TEXT = "Select file...";

    private final Controller controller;
    private final Stage primaryStage;
    private Optional<Path> filesDirectoryPath;
    private Optional<Path> stopwordsFilePath;
    @FXML
    private BarChart<String, Long> barChart;
    @FXML
    private Label filesDirectoryLabel;
    @FXML
    private Button filesDirectoryButton;
    @FXML
    private Spinner<Integer> numberWordsSpinner;
    @FXML
    private Label stopwordsFileLabel;
    @FXML
    private Button stopwordsFileButton;
    @FXML
    private Button startButton;
    @FXML
    private Button suspendButton;
    @FXML
    private Button resetButton;
    @FXML
    private Label processedWordsLabel;

    public GUIView(final Stage primaryStage) {
        this.controller = new ControllerImpl(this);
        this.primaryStage = Objects.requireNonNull(primaryStage);
        this.filesDirectoryPath = Optional.empty();
        this.stopwordsFilePath = Optional.empty();
        this.show();
    }

    @Override
    public void displayProgress(final Map<String, Long> frequencies, final long processedWords) {
        Platform.runLater(() -> {
            final ObservableList<XYChart.Series<String, Long>> data = this.barChart.getData();
            final XYChart.Series<String, Long> series = new XYChart.Series<>();
            frequencies.entrySet()
                       .stream()
                       .map(e -> new XYChart.Data<>(e.getKey(), e.getValue()))
                       .forEach(d -> series.getData().add(d));
            data.clear();
            data.add(series);
            this.processedWordsLabel.setText(String.format(PROCESSED_WORDS_LABEL_MSG, processedWords));
        });
    }

    @Override
    public void displayCompletion() {
        Platform.runLater(() -> {
            this.suspendButton.setDisable(true);
            this.resetButton.setDisable(false);
        });
    }

    @Override
    public void displayError(final String message) {
        Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, message, ButtonType.OK).showAndWait());
    }

    private void show() {
        Platform.runLater(() -> {
            try {
                this.filesDirectoryPath = Optional.empty();
                this.stopwordsFilePath = Optional.empty();
                final FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource(FXML_FILENAME));
                loader.setController(this);
                final BorderPane borderPane = loader.load();
                this.setFilesDirectoryControls();
                this.setStopwordsFileControls();
                this.startButton.setOnMouseClicked(e -> {
                    if (this.filesDirectoryPath.isPresent()) {
                        if (this.stopwordsFilePath.isPresent()) {
                            this.startButton.setDisable(true);
                            this.suspendButton.setDisable(false);
                            this.controller.launch(this.filesDirectoryPath.get(),
                                                   this.stopwordsFilePath.get(),
                                                   this.numberWordsSpinner.getValue());
                        } else {
                            this.displayError(STOPWORDS_FILE_ERROR_MSG);
                        }
                    } else {
                        this.displayError(PDFS_FOLDER_ERROR_MSG);
                    }
                });
                this.suspendButton.setOnMouseClicked(e -> {
                    //TODO: switch to resume behavior
                    this.controller.suspend();
                });
                this.resetButton.setOnMouseClicked(e -> {
                    this.barChart.getData().clear();
                    this.processedWordsLabel.setText(String.format(PROCESSED_WORDS_LABEL_MSG, 0));
                    this.stopwordsFileLabel.setText(FILE_NOT_CHOSEN_TEXT);
                    this.filesDirectoryLabel.setText(FILE_NOT_CHOSEN_TEXT);
                    this.startButton.setDisable(false);
                    this.suspendButton.setDisable(true);
                    this.resetButton.setDisable(true);
                });
                final Scene scene = new Scene(borderPane);
                this.primaryStage.setScene(scene);
                this.primaryStage.sizeToScene();
                this.primaryStage.setTitle(APP_TITLE);
                this.primaryStage.show();
                this.primaryStage.centerOnScreen();
                this.primaryStage.setMinWidth(this.primaryStage.getWidth());
                this.primaryStage.setMinHeight(this.primaryStage.getHeight());
            } catch (final IOException ex) {
                this.displayError(String.format(FXML_FILE_ERROR_MSG, ex.getMessage()));
            }
        });
    }

    private void setFilesDirectoryControls() {
        this.filesDirectoryButton.setOnMouseClicked(e -> {
            final DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle(PDFS_FOLDER_CHOOSER_TITLE);
            directoryChooser.setInitialDirectory(new File(INITIAL_DIRECTORY));
            final File file = directoryChooser.showDialog(this.primaryStage);
            this.filesDirectoryPath = Optional.ofNullable(file).map(File::toPath);
            if (file != null) {
                this.filesDirectoryLabel.setText(file.toString());
            }
        });
    }

    private void setStopwordsFileControls() {
        this.stopwordsFileButton.setOnMouseClicked(e -> {
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(STOPWORDS_FILE_CHOOSER_TITLE);
            fileChooser.setInitialDirectory(new File(INITIAL_DIRECTORY));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(FILE_CHOOSER_DESC, FILE_CHOOSER_TXT_EXT));
            final File file = fileChooser.showOpenDialog(this.primaryStage);
            this.stopwordsFilePath = Optional.ofNullable(file).map(File::toPath);
            if (file != null) {
                this.stopwordsFileLabel.setText(file.toString());
            }
        });
    }
}
