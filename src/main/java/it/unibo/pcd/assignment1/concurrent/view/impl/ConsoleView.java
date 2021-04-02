package it.unibo.pcd.assignment1.concurrent.view.impl;

import it.unibo.pcd.assignment1.ChronometerImpl;
import it.unibo.pcd.assignment1.concurrent.controller.Controller;
import it.unibo.pcd.assignment1.concurrent.controller.impl.ControllerImpl;
import it.unibo.pcd.assignment1.concurrent.view.View;
import it.unibo.pcd.assignment1.Chronometer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ConsoleView implements View {
    private static final String NOT_ENOUGH_ARGUMENTS_ERROR = "Not enough arguments";
    private static final String APP_TITLE = "\n--- Statistics for given files ---\n";
    private static final String FREQUENCIES_TITLE = "The %d most frequent words:\n";
    private static final String FREQUENCIES_LINE = "- \"%s\": %d\n";
    private static final String PROCESSED_WORDS_TITLE = "\nTotal processed words:\n- %d\n";
    private static final String ERROR_MSG_PREFIX = "An error has occured: %s\n";
    private static final String END_MESSAGE = "\n--- End ---";
    public static final String ELAPSED_TIME_TITLE = "\nElapsed time: %f\n";

    private final Controller controller;
    private final Path filesDirectory;
    private final int numberWords;
    private final Path stopwordsFile;
    private final Chronometer chronometer;
    private final boolean printTimings;
    private long lastProcessedWords;
    private Map<String, Long> lastFrequencies;

    public ConsoleView(final List<String> arguments) {
        this.controller = new ControllerImpl(this);
        if (arguments.size() < 3) {
            System.err.println(NOT_ENOUGH_ARGUMENTS_ERROR);
            this.controller.exit();
        }
        this.filesDirectory = Paths.get(arguments.get(0));
        this.numberWords = Integer.parseInt(arguments.get(1));
        this.stopwordsFile = Paths.get(arguments.get(2));
        this.printTimings = Optional.of(arguments)
                                    .filter(a -> a.size() > 3)
                                    .map(a -> Boolean.parseBoolean(a.get(3)))
                                    .orElse(false);
        this.chronometer = new ChronometerImpl();
        this.show();
    }

    @Override
    public void displayProgress(final Map<String, Long> frequencies, final long processedWords) {
        this.lastFrequencies = frequencies;
        this.lastProcessedWords = processedWords;
    }

    @Override
    public void displayCompletion() {
        if (this.printTimings) {
            this.chronometer.stop();
            System.out.printf(ELAPSED_TIME_TITLE, this.chronometer.getTime() / 1000.0);
        }
        System.out.println(APP_TITLE);
        System.out.printf(FREQUENCIES_TITLE, this.lastFrequencies.size());
        this.lastFrequencies.forEach((w, f) -> System.out.printf(FREQUENCIES_LINE, w, f));
        System.out.printf(PROCESSED_WORDS_TITLE, this.lastProcessedWords);
        System.out.println(END_MESSAGE);
    }

    @Override
    public void displayError(final String message) {
        System.err.printf(ERROR_MSG_PREFIX, message);
    }

    private void show() {
        if (this.printTimings) {
            this.chronometer.start();
        }
        this.controller.launch(this.filesDirectory, this.stopwordsFile, this.numberWords);
    }
}
