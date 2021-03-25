package it.unibo.pcd.assignment1.model.tasks.impl;

import it.unibo.pcd.assignment1.controller.agents.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.controller.agents.impl.FilterAgent;
import it.unibo.pcd.assignment1.controller.agents.impl.TaskIterator;
import it.unibo.pcd.assignment1.model.pipes.Pipe;
import it.unibo.pcd.assignment1.model.pipes.impl.BoundedPipe;
import it.unibo.pcd.assignment1.model.pipes.impl.UnboundedPipe;
import it.unibo.pcd.assignment1.model.pipes.impl.WordCounter;
import it.unibo.pcd.assignment1.model.tasks.Task;
import it.unibo.pcd.assignment1.model.updates.Update;
import it.unibo.pcd.assignment1.view.View;
import it.unibo.pcd.assignment1.wrapper.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

public class ControllerTask implements Task {
    private static final int TOTAL_THREADS = Runtime.getRuntime().availableProcessors() + 1; //TODO: change
    private static final int PIPE_MAX_NUMBER = 1000;

    private final View view;
    private final Path filesDirectory;
    private final Path stopwordsFile;
    private final AgentSuspendedFlag suspendedFlag;
    private final Pipe<Path> paths;
    private final Pipe<Document> documents;
    private final Pipe<String> pages;
    private final Pipe<Update> updates;

    public ControllerTask(final View view,
                          final Path filesDirectory,
                          final Path stopwordsFile,
                          final int wordsNumber,
                          final AgentSuspendedFlag suspendedFlag) {
        this.view = Objects.requireNonNull(view);
        this.filesDirectory = Objects.requireNonNull(filesDirectory);
        this.stopwordsFile = Objects.requireNonNull(stopwordsFile);
        this.suspendedFlag = Objects.requireNonNull(suspendedFlag);
        this.paths = new UnboundedPipe<>();
        this.documents = new BoundedPipe<>(PIPE_MAX_NUMBER);
        this.pages = new BoundedPipe<>(PIPE_MAX_NUMBER);
        this.updates = new WordCounter(PIPE_MAX_NUMBER, wordsNumber);
    }

    @Override
    public void run() {
        try {
            Files.list(this.filesDirectory)
                 .filter(p -> p.toString().matches(".*pdf$"))
                 .forEach(this.paths::enqueue);
            final Set<String> stopwords = new HashSet<>(Files.readAllLines(this.stopwordsFile));
            IntStream.range(0, TOTAL_THREADS)
                     .boxed()
                     .map(i -> i % TOTAL_THREADS)
                     .forEach(e -> {
                         switch (e) {
                             case 1:
                                 new FilterAgent(new TaskIterator(
                                     TaskIterator.InitialTask.PATH,
                                     this.paths,
                                     this.documents,
                                     this.pages,
                                     this.updates,
                                     this.suspendedFlag,
                                     stopwords
                                 )).execute();
                                 break;
                             case 2:
                                 new FilterAgent(new TaskIterator(
                                     TaskIterator.InitialTask.DOCUMENT,
                                     this.paths,
                                     this.documents,
                                     this.pages,
                                     this.updates,
                                     this.suspendedFlag,
                                     stopwords
                                 )).execute();
                                 break;
                             case 3:
                                 new FilterAgent(new TaskIterator(
                                     TaskIterator.InitialTask.PAGE,
                                     this.paths,
                                     this.documents,
                                     this.pages,
                                     this.updates,
                                     this.suspendedFlag,
                                     stopwords
                                 )).execute();
                                 break;
                         }
                     });
            do {
                final Optional<Update> update = this.updates.dequeue();
                if (!update.isPresent()) {
                    break;
                }
                this.view.displayProgress(update.get().getFrequencies(), update.get().getProcessedWords());
            } while (true);
            this.view.displayCompletion();
        } catch (final Exception ex) {
            this.view.displayError(ex.getMessage());
        }
    }
}
