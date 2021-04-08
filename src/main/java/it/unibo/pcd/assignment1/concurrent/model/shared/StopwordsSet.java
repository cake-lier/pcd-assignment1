package it.unibo.pcd.assignment1.concurrent.model.shared;

import java.util.Collection;
import java.util.Set;

/**
 * The set of the stopwords as loaded from a given file.
 */
public interface StopwordsSet {
    /**
     * It sets the collection of words that should be used as the set of stopwords. Once set, it could not be reset.
     * @param stopwords the collection of words that should be used as the set of stopwords
     */
    void set(Collection<String> stopwords);

    /**
     * It returns the set of words which are stopwords.
     * @return the set of words which are stopwords
     */
    Set<String> get();
}
