package it.unibo.pcd.assignment1.model.update;

import java.util.Collections;
import java.util.Map;

public class UpdateFactory {
    private static final Update EMPTY_UPDATE = new UpdateImpl(Collections.emptyMap(), 0);

    private UpdateFactory() {}

    public static Update of(final Map<String, Integer> frequencies, final int processedWords) {
        return new UpdateImpl(frequencies, processedWords);
    }

    public static Update empty() {
        return EMPTY_UPDATE;
    }
}
