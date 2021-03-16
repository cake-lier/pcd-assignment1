package it.unibo.pcd.assignment1.model.update;

import java.util.Map;

public interface Update {
    Map<String, Integer> getFrequencies();

    int getProcessedWords();
}
