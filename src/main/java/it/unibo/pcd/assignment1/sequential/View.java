package it.unibo.pcd.assignment1.sequential;

import it.unibo.pcd.assignment1.Chronometer;
import it.unibo.pcd.assignment1.ChronometerImpl;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * The View component of the sequential application. It should capture user input and be notified of changes into the Model
 * component which should appear to the user.
 */
public class View {
    private static final String NOT_ENOUGH_ARGUMENTS_ERROR = "Not enough arguments";
    private static final String APP_TITLE = "\n--- Statistics for given files ---\n";
    private static final String FREQUENCIES_TITLE = "The %d most frequent words:\n";
    private static final String FREQUENCIES_LINE = "- %s: %d\n";
    private static final String PROCESSED_WORDS_TITLE = "\nTotal processed words:\n- %d\n";
    private static final String ERROR_MSG_PREFIX = "An error has occurred: %s\n";
    private static final String ELAPSED_TIME_TITLE = "\nElapsed time: %f\n";
    private static final String END_MESSAGE = "\n--- End ---";

    private final Chronometer chronometer;

    /**
     * Default constructor.
     * @param arguments the list of arguments passed to the application at launch
     */
    public View(final List<String> arguments) {
        final Controller controller = new Controller(this);
        if (arguments.size() < 3) {
            System.err.println(NOT_ENOUGH_ARGUMENTS_ERROR);
            controller.exit();
        }
        this.chronometer = new ChronometerImpl();
        this.chronometer.start();
        controller.launch(Paths.get(arguments.get(0)), Paths.get(arguments.get(2)), Integer.parseInt(arguments.get(1)));
    }

    /**
     * It displays the completion of the computation, that the computation has ended.
     */
    public void displayCompletion(final Map<String, Long> frequencies, final int processedWords) {
        this.chronometer.stop();
        System.out.printf(ELAPSED_TIME_TITLE, this.chronometer.getTime() / 1000.0);
        System.out.println(APP_TITLE);
        System.out.printf(FREQUENCIES_TITLE, frequencies.size());
        frequencies.forEach((w, f) -> System.out.printf(FREQUENCIES_LINE, w, f));
        System.out.printf(PROCESSED_WORDS_TITLE, processedWords);
        System.out.println(END_MESSAGE);
    }

    /**
     * It displays an error message given the text of the message itself.
     * @param message the text of the error message to display
     */
    public void displayError(final String message) {
        System.err.printf(ERROR_MSG_PREFIX, message);
    }
}
