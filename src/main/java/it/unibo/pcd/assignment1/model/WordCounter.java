package it.unibo.pcd.assignment1.model;

import it.unibo.pcd.assignment1.model.update.Update;

public interface WordCounter {
    void countWord(String word, boolean isToAdd);

    void suspend();

    void resume();

    Update getCurrentState();
}
