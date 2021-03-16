package it.unibo.pcd.assignment1.view;

import java.util.Map;

public interface View {
    void show();

    void update(Map<String, Integer> frequencies, int processedWords);

    void displayError(String message);
}
