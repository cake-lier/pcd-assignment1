package it.unibo.pcd.assignment1.controller;

import java.nio.file.Path;

public interface Controller {
    void launch(Path filesDirectory, Path stopwordsFile, final int wordsNumber);

    void suspend();

    void resume();

    void exit();
}
