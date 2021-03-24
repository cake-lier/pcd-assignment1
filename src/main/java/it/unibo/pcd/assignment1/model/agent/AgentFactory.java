package it.unibo.pcd.assignment1.model.agent;

import it.unibo.pcd.assignment1.view.View;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;

public interface AgentFactory {

    public Agent pathAgent(final Set<String> stopwords);

    public Agent documentAgent(final Set<String> stopwords);
    public Agent pageAgent(final Set<String> stopwords);

    public Agent controllerAgent(final View view, final Path directories, final Path stopwards);
}
