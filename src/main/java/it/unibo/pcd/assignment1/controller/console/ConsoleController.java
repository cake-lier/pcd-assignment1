package it.unibo.pcd.assignment1.controller.console;

import it.unibo.pcd.assignment1.controller.Controller;
import it.unibo.pcd.assignment1.model.Model;
import it.unibo.pcd.assignment1.model.ModelImpl;
import it.unibo.pcd.assignment1.model.update.Update;
import it.unibo.pcd.assignment1.view.View;
import it.unibo.pcd.assignment1.view.console.ConsoleView;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ConsoleController implements Controller {
    private final Model model;
    private final View view;

    public ConsoleController(final List<String> arguments) {
        this.model = new ModelImpl();
        this.view = new ConsoleView(this, arguments);
        this.view.show();
    }

    @Override
    public void launch(final Path filesDirectory, final Path stopwordsFile, final int wordsNumber) {
        try {
            this.model.startCalculation(filesDirectory, stopwordsFile, wordsNumber);
            this.model.awaitResult();
            final Update result = this.model.getUpdate();
            this.view.update(result.getFrequencies(), result.getProcessedWords());
        } catch (final IOException ex) {
            this.view.displayError(ex.getMessage());
        }
    }

    @Override
    public void suspend() {
        this.model.suspendCalculation();
    }

    @Override
    public void resume() {
        this.model.resumeCalculation();
    }

    @Override
    public void exit() {
        System.exit(0);
    }
}
