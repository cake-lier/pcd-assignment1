package it.unibo.pcd.assignment1.jpf.model.entities;

import java.util.Map;

public interface Update {
    Map<String, Long> getFrequencies();

    long getProcessedWords();
}
