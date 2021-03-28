package it.unibo.pcd.assignment1.concurrent.view;

import java.util.Map;

public interface View {
    void displayProgress(Map<String, Long> frequencies, long processedWords);

    void displayCompletion();

    void displayError(String message);
}
