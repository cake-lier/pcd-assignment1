package it.unibo.pcd.assignment1.main;
import it.unibo.pcd.assignment1.view.console.ConsoleView;

import java.util.Arrays;

public class ConsoleMain {
    private ConsoleMain() {}

    public static void main(final String[] args) {
        new ConsoleView(Arrays.asList(args));
    }
}
