package it.unibo.pcd.assignment1.sequential;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class View {
    private static final String NOT_ENOUGH_ARGUMENTS_ERROR = "Not enough arguments";
    private static final String FREQUENCIES_TITLE = "\nThe %d most frequent words:\n";
    private static final String FREQUENCIES_LINE = "- %s: %d\n";
    private static final String PROCESSED_WORDS_TITLE = "\nTotal processed words:\n- %d";
    private static final String ERROR_MSG_PREFIX = "An error has occured: %s\n";

    private final Chronometer chronometer;

    public View(final List<String> arguments) {
        final Controller controller = new Controller(this);
        if (arguments.size() < 3) {
            System.err.println(NOT_ENOUGH_ARGUMENTS_ERROR);
            controller.exit();
        }
        this.chronometer = new Chronometer();
        this.chronometer.start();
        controller.launch(Paths.get(arguments.get(0)), Paths.get(arguments.get(2)), Integer.parseInt(arguments.get(1)));
    }

    public void displayProgress() {
        System.out.print(".");
    }

    public void displayCompletion(final Map<String, Long> frequencies, final int processedWords) {
        final long elapsedMillis = this.chronometer.stop();
        System.out.flush();
        System.out.printf(FREQUENCIES_TITLE, frequencies.size());
        frequencies.forEach((w, f) -> System.out.printf(FREQUENCIES_LINE, w, f));
        System.out.printf(PROCESSED_WORDS_TITLE, processedWords);
        System.out.println("\nElapsed time: " + elapsedMillis / 1000.0);
    }

    public void displayError(final String message) {
        System.err.printf(ERROR_MSG_PREFIX, message);
    }
}
