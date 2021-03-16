package it.unibo.pcd.assignment1.model.update;

import java.util.*;

class UpdateImpl implements Update {
    private final Map<String, Integer> frequencies;
    private final int processedWords;

    protected UpdateImpl(final Map<String, Integer> frequencies, final int processedWords) {
        this.frequencies = Collections.unmodifiableMap(new LinkedHashMap<>(frequencies));
        this.processedWords = processedWords;
    }

    @Override
    public Map<String, Integer> getFrequencies() {
        return this.frequencies;
    }

    @Override
    public int getProcessedWords() {
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
