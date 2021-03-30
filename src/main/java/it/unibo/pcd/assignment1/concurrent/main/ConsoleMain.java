package it.unibo.pcd.assignment1.concurrent.main;
import it.unibo.pcd.assignment1.concurrent.view.impl.ConsoleView;

import java.util.Arrays;

public class ConsoleMain {
    private ConsoleMain() {}

    public static void main(final String[] args) {
        new ConsoleView(Arrays.asList(args));
    }
}
