package it.unibo.pcd.assignment1.jpf.model.entities.impl;

import it.unibo.pcd.assignment1.jpf.model.entities.Update;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class UpdateImpl implements Update {
    private final Map<String, Long> frequencies;
    private final long processedWords;

    public UpdateImpl(final Map<String, Long> frequencies, final long processedWords) {
        this.frequencies = Collections.unmodifiableMap(frequencies);
        this.processedWords = processedWords;
    }

    @Override
    public Map<String, Long> getFrequencies() {
        return this.frequencies;
    }

    @Override
    public long getProcessedWords() {
        return this.processedWords;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final UpdateImpl update = (UpdateImpl) o;
        return this.processedWords == update.processedWords && this.frequencies.equals(update.frequencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.frequencies, this.processedWords);
    }

    @Override
    public String toString() {
        return "UpdateImpl{frequencies=" + this.frequencies + ", processedWords=" + this.processedWords + '}';
    }
}
