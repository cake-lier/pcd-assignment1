package it.unibo.pcd.assignment1;

import it.unibo.pcd.assignment1.concurrent.controller.agents.Agent;
import it.unibo.pcd.assignment1.concurrent.controller.agents.impl.AgentImpl;
import it.unibo.pcd.assignment1.concurrent.controller.tasks.impl.UpdateSinkTask;
import it.unibo.pcd.assignment1.concurrent.model.pipes.WordCounter;
import it.unibo.pcd.assignment1.concurrent.model.pipes.impl.WordCounterImpl;
import it.unibo.pcd.assignment1.concurrent.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.concurrent.model.shared.impl.AgentSuspendedFlagImpl;
import it.unibo.pcd.assignment1.concurrent.model.tasks.FilterTaskTypes;
import it.unibo.pcd.assignment1.concurrent.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.concurrent.model.tasks.impl.TaskCounterImpl;
import it.unibo.pcd.assignment1.concurrent.model.tasks.impl.TaskListFactory;
import it.unibo.pcd.assignment1.concurrent.view.View;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ModelTest implements View {
    private static final int PIPES_SIZE = 100;
    private static final int WORDS_NUMBER = 5;
    private static final int TOTAL_THREADS = Runtime.getRuntime().availableProcessors() + 1;
    //TODO: use formula Ncpu * Ucpu * (1 + w/c)
    private static final String PDFS_FOLDER_NAME = "pdfs";
    private static final String STOPWORDS_FILE_NAME = "stopwords.txt";

    private final Semaphore awaitCompletion = new Semaphore(0);
    private Map<String, Long> latestFrequencies;
    private Map<String, Long> frequencies;
    private long latestProcessedWords;
    private long processedWords;

    @BeforeEach
    void setUp() {
        this.processedWords = 2581;
        this.frequencies = new LinkedHashMap<>();
        this.frequencies.put("dolor", 610L);
        this.frequencies.put("ipsum", 325L);
        this.frequencies.put("lorem", 315L);
        this.frequencies.put("di", 200L);
        this.frequencies.put("frase", 200L);
        this.latestFrequencies = new LinkedHashMap<>();
        this.latestProcessedWords = 0;
    }

    @Test
    void testExampleCase() throws URISyntaxException, InterruptedException {
        final WordCounter updates = new WordCounterImpl(PIPES_SIZE, WORDS_NUMBER);
        final AgentSuspendedFlag suspendedFlag = new AgentSuspendedFlagImpl();
        final TaskCounter taskCounter = new TaskCounterImpl();
        final Consumer<Exception> exceptionHandler = e -> this.displayError(e.getMessage());
        final TaskListFactory taskListFactory = new TaskListFactory(PIPES_SIZE, updates, suspendedFlag, taskCounter);
        Stream.concat(Stream.of(new AgentImpl(taskListFactory.createForGeneratorAgent(
                                                  Paths.get(ClassLoader.getSystemResource(PDFS_FOLDER_NAME).toURI()),
                                                  Paths.get(ClassLoader.getSystemResource(STOPWORDS_FILE_NAME).toURI())
                                              ),
                                              exceptionHandler),
                                new AgentImpl(Collections.singletonList(new UpdateSinkTask(updates,
                                                                                           this,
                                                                                           suspendedFlag,
                                                                                           taskCounter)),
                                              exceptionHandler)),
                      Stream.generate(() -> Arrays.asList(FilterTaskTypes.values()))
                            .flatMap(List::stream)
                            .limit(Math.max(TOTAL_THREADS - 2, FilterTaskTypes.values().length))
                            .map(t -> new AgentImpl(taskListFactory.createForFilterAgent(t), exceptionHandler)))
              .forEach(Agent::go);
        this.awaitCompletion.acquire();
        Assertions.assertEquals(this.frequencies, this.latestFrequencies);
        Assertions.assertEquals(this.processedWords, this.latestProcessedWords);
    }

    @Override
    public void displayProgress(final Map<String, Long> frequencies, final long processedWords) {
        this.latestFrequencies = frequencies;
        this.latestProcessedWords = processedWords;
    }

    @Override
    public void displayCompletion() {
        this.awaitCompletion.release();
    }

    @Override
    public void displayError(final String message) {
        System.err.println(message);
    }
}
