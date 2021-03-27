package it.unibo.pcd.assignment1.model.tasks.impl;

import it.unibo.pcd.assignment1.controller.agents.Task;
import it.unibo.pcd.assignment1.model.pipes.Pipe;
import it.unibo.pcd.assignment1.model.pipes.impl.WordCounter;
import it.unibo.pcd.assignment1.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.model.shared.StopwordsSet;
import it.unibo.pcd.assignment1.model.tasks.FilterTaskTypes;
import it.unibo.pcd.assignment1.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.model.updates.Update;
import it.unibo.pcd.assignment1.model.entities.DocumentImpl;
import it.unibo.pcd.assignment1.model.entities.Page;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TaskListFactory {

    public static List<Task> createForFilterAgent(final FilterTaskTypes firstTaskType,
                                                  final Pipe<Path> paths,
                                                  final Pipe<DocumentImpl> documents,
                                                  final Pipe<Page> pages,
                                                  final Pipe<Update> updates,
                                                  final AgentSuspendedFlag suspendedFlag,
                                                  final TaskCounter taskCounter,
                                                  final StopwordsSet stopwords) {
        return Arrays.<Supplier<Task>>asList(() -> new PathFilterTask(paths, documents, suspendedFlag, taskCounter),
                                             () -> new DocumentFilterTask(documents, pages, suspendedFlag, taskCounter),
                                             () -> new PageFilterTask(pages, updates, stopwords, suspendedFlag, taskCounter))
                     .subList(firstTaskType.ordinal(), 3)
                     .stream()
                     .map(Supplier::get)
                     .collect(Collectors.toList());
    }

    public static List<Task> createForGeneratorAgent(final Path filesDirectory,
                                                     final Path stopwordsFile,
                                                     final AgentSuspendedFlag suspendedFlag,
                                                     final TaskCounter taskCounter,
                                                     final Pipe<Path> paths,
                                                     final Pipe<DocumentImpl> documents,
                                                     final Pipe<Page> pages,
                                                     final Pipe<Update> updates,
                                                     final StopwordsSet stopwords) {
        final List<Task> taskList = new ArrayList<>(
            createForFilterAgent(FilterTaskTypes.PATH, paths, documents, pages, updates, suspendedFlag, taskCounter, stopwords)
        );
        taskList.add(0, new PathGeneratorTask(filesDirectory, stopwordsFile, paths, suspendedFlag, taskCounter, stopwords));
        return taskList;
    }

    public static List<Task> createForSinkAgent(final WordCounter wordCounter,
                                                final Consumer<Update> updatesSink,
                                                final Runnable completionNotifier,
                                                final AgentSuspendedFlag suspendedFlag,
                                                final TaskCounter ticketManager) {
        return Collections.singletonList(new UpdateSinkTask(wordCounter,
                                                            updatesSink,
                                                            completionNotifier,
                                                            suspendedFlag,
                                                            ticketManager));
    }
}
