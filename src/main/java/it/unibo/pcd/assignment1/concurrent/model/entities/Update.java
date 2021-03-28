package it.unibo.pcd.assignment1.concurrent.model.entities;

import java.util.Map;

public interface Update {
    Map<String, Long> getFrequencies();

    long getProcessedWords();
}
