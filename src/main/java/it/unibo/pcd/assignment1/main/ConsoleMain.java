package it.unibo.pcd.assignment1.main;

import it.unibo.pcd.assignment1.controller.console.ConsoleController;

import java.util.Arrays;

public class ConsoleMain {
    private ConsoleMain() {}

    public static void main(final String[] args) {
        new ConsoleController(Arrays.asList(args));
    }
}
