package it.unibo.pcd.assignment1.model;

import it.unibo.pcd.assignment1.model.update.Update;

import java.io.IOException;
import java.nio.file.Path;

public interface Model {
    void startCalculation(final Path filesDirectory, final Path stopwordsFile, final int wordsNumber) throws IOException;

    void suspendCalculation();

    void resumeCalculation();

    boolean isCalculationCompleted();

    void awaitResult();

    Update getUpdate();
}
