package it.unibo.pcd.assignment1.model.updates;

import java.util.Map;

public interface Update {
    Map<String, Long> getFrequencies();

    long getProcessedWords();
}
