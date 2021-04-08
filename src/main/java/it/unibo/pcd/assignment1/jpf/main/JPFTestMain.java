package it.unibo.pcd.assignment1.jpf.main;

import it.unibo.pcd.assignment1.jpf.model.pipes.WordCounter;
import it.unibo.pcd.assignment1.jpf.model.pipes.impl.WordCounterImpl;
import it.unibo.pcd.assignment1.jpf.model.tasks.FilterTaskTypes;
import it.unibo.pcd.assignment1.jpf.model.tasks.impl.TaskListFactory;
import it.unibo.pcd.assignment1.jpf.controller.agents.Agent;
import it.unibo.pcd.assignment1.jpf.controller.agents.impl.AgentImpl;
import it.unibo.pcd.assignment1.jpf.controller.tasks.impl.UpdateSinkTask;
import it.unibo.pcd.assignment1.jpf.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.jpf.model.tasks.impl.TaskCounterImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class JPFTestMain {
    private static final int PIPES_SIZE = 1000;
    private static final int TOTAL_THREADS = Math.round(Runtime.getRuntime().availableProcessors() * 1.0f * (1 + 1.093f));
    private static final int WORDS_FOR_PAGE = 2;
    private static final int DOCUMENTS_NUMBER = 2;
    private static final int PAGES_FOR_DOCUMENT = 2;
    private static final String WORD = "word";

    private JPFTestMain() {}

    public static void main(String[] args) {
        final TaskCounter taskCounter = new TaskCounterImpl();
        final WordCounter updates = new WordCounterImpl(PIPES_SIZE);
        final TaskListFactory taskListFactory = new TaskListFactory(DOCUMENTS_NUMBER,
                                                                    WORDS_FOR_PAGE,
                                                                    PAGES_FOR_DOCUMENT,
                                                                    PIPES_SIZE,
                                                                    updates,
                                                                    taskCounter,
                                                                    WORD);
        Stream.concat(Stream.of(new AgentImpl(taskListFactory.createForGeneratorAgent()),
                                new AgentImpl(Collections.singletonList(
                                    new UpdateSinkTask(updates,
                                                       taskCounter,
                                                       DOCUMENTS_NUMBER * PAGES_FOR_DOCUMENT * WORDS_FOR_PAGE,
                                                       WORD)
                                ))),
                      Stream.generate(() -> Arrays.asList(FilterTaskTypes.values()))
                            .flatMap(List::stream)
                            .limit(TOTAL_THREADS - 2)
                            .map(t -> new AgentImpl(taskListFactory.createForFilterAgent(t))))
              .forEach(Agent::go);
    }
}
