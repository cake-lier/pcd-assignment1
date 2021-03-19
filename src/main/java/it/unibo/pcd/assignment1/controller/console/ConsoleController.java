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
import java.util.Optional;

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
            do {
                final Optional<Update> update = this.model.getLatestUpdate();
                if (!update.isPresent()) {
                    break;
                }
                this.view.displayProgress(update.get().getFrequencies(), update.get().getProcessedWords());
            } while (true);
            this.view.displayCompletion();
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
