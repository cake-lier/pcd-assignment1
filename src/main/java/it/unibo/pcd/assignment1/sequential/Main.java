package it.unibo.pcd.assignment1.sequential;

import java.util.Arrays;

/**
 * The main class for the sequential application.
 */
public class Main {
    private Main() {}

    /**
     * The main method.
     * @param args the arguments the application receives as input. These should be, in order, the path to the directory
     *             containing the PDF to process, the number of most frequent words to display and the path to the file
     *             containing the stopwords.
     */
    public static void main(final String[] args) {
        new View(Arrays.asList(args));
    }
}
