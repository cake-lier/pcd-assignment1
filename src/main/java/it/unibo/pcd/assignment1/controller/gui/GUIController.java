package it.unibo.pcd.assignment1.controller.gui;

import it.unibo.pcd.assignment1.controller.Controller;
import it.unibo.pcd.assignment1.model.Model;
import it.unibo.pcd.assignment1.model.ModelImpl;
import it.unibo.pcd.assignment1.model.update.Update;
import it.unibo.pcd.assignment1.view.View;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

public class GUIController implements Controller {
    private final Model model;
    private final View view;

    public GUIController(final View view) {
        this.model = new ModelImpl();
        this.view = Objects.requireNonNull(view);
        this.view.show();
    }

    @Override
    public void launch(final Path filesDirectory, final Path stopwordsFile, final int wordsNumber) {
        try {
            this.model.startCalculation(filesDirectory, stopwordsFile, wordsNumber);
            new Thread(() -> {
                do {
                    final Optional<Update> optionalUpdate = this.model.getLatestUpdate();
                    if (!optionalUpdate.isPresent()) {
                        break;
                    }
                    final Update update = optionalUpdate.get();
                    this.view.displayProgress(update.getFrequencies(), update.getProcessedWords());
                } while (true);
                this.view.displayCompletion();
            }).start();
        } catch (final IOException ex) {
            ex.printStackTrace();
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
