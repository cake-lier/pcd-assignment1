package it.unibo.pcd.assignment1.view.console;

import it.unibo.pcd.assignment1.controller.Controller;
import it.unibo.pcd.assignment1.controller.MioController;
import it.unibo.pcd.assignment1.view.View;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ConsoleView implements View {
    public static final String NOT_ENOUGH_ARGUMENTS_ERROR = "Not enough arguments";
    public static final String APP_TITLE = "--- Statistics for given files ---\n";
    public static final String FREQUENCIES_TITLE = "The %d most frequent words:\n";
    public static final String FREQUENCIES_LINE = "- %s: %d\n";
    public static final String PROCESSED_WORDS_TITLE = "\nTotal processed words:\n- %d";
    public static final String ERROR_MSG_PREFIX = "An error has occured: %s\n";

    private final Controller controller;
    private final Path filesDirectory;
    private final int numberWords;
    private final Path stopwordsFile;

    public ConsoleView(final List<String> arguments) {
        this.controller = new MioController(this);
        if (arguments.size() < 3) {
            System.err.println(NOT_ENOUGH_ARGUMENTS_ERROR);
            this.controller.exit();
        }
        this.filesDirectory = Paths.get("C:\\Users\\Lorenzo\\pdfs");//Paths.get(arguments.get(0));
        this.numberWords = 5;//Integer.parseInt(arguments.get(1));
        this.stopwordsFile = Paths.get("C:\\Users\\Lorenzo\\stopwords.txt");//Paths.get(arguments.get(2));
    }

    @Override
    public void show() {
        System.out.println(APP_TITLE);
        this.controller.launch(this.filesDirectory, this.stopwordsFile, this.numberWords);
    }

    @Override
    public void displayProgress(final Map<String, Long> frequencies, final long processedWords) {
        System.out.printf(FREQUENCIES_TITLE, frequencies.size());
        frequencies.forEach((w, f) -> System.out.printf(FREQUENCIES_LINE, w, f));
        System.out.printf(PROCESSED_WORDS_TITLE, processedWords);
    }

    @Override
    public void displayCompletion() {
        //TODO: fix view
    }

    @Override
    public void displayError(final String message) {
        System.err.printf(ERROR_MSG_PREFIX, message);
    }
}
