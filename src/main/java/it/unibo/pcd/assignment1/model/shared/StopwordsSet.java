package it.unibo.pcd.assignment1.model.shared;

import java.util.Collection;
import java.util.Set;

public interface StopwordsSet {
    void set(Collection<String> stopwords);

    Set<String> get();
}
