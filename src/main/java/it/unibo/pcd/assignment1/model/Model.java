package it.unibo.pcd.assignment1.model;

import it.unibo.pcd.assignment1.model.update.Update;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public interface Model {
    void startCalculation(final Path filesDirectory, final Path stopwordsFile, final int wordsNumber) throws IOException;

    void suspendCalculation();

    void resumeCalculation();

    Optional<Update> getLatestUpdate();
}
