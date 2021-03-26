package it.unibo.pcd.assignment1.model.tasks.impl;

import it.unibo.pcd.assignment1.controller.agents.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.controller.agents.AgentTicketManager;
import it.unibo.pcd.assignment1.controller.agents.impl.FilterAgent;
import it.unibo.pcd.assignment1.controller.agents.impl.GeneratorAgent;
import it.unibo.pcd.assignment1.controller.agents.impl.TaskIterator;
import it.unibo.pcd.assignment1.model.pipes.Pipe;
import it.unibo.pcd.assignment1.model.pipes.impl.BoundedPipe;
import it.unibo.pcd.assignment1.model.pipes.impl.UnboundedPipe;
import it.unibo.pcd.assignment1.model.pipes.impl.WordCounter;
import it.unibo.pcd.assignment1.model.updates.Update;
import it.unibo.pcd.assignment1.view.View;
import it.unibo.pcd.assignment1.wrapper.Document;
import it.unibo.pcd.assignment1.wrapper.Page;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ControllerTask extends AbstractTask{
    private static final int PIPE_MAX_NUMBER = 100000;

    private final View view;
    private final Path filesDirectory;
    private final Path stopwordsFile;
    private final Pipe<Path> paths;
    private final Pipe<Document> documents;
    private final Pipe<Page> pages;
    private final Pipe<Update> updates;

    public ControllerTask(final View view,
                          final Path filesDirectory,
                          final Path stopwordsFile,
                          final int wordsNumber,
                          final AgentSuspendedFlag suspendedFlag,
                          final AgentTicketManager ticketManager) {
        super(suspendedFlag, ticketManager);
        this.view = Objects.requireNonNull(view);
        this.filesDirectory = Objects.requireNonNull(filesDirectory);
        this.stopwordsFile = Objects.requireNonNull(stopwordsFile);
        this.paths = new UnboundedPipe<>();
        this.documents = new BoundedPipe<>(PIPE_MAX_NUMBER);
        this.pages = new BoundedPipe<>(PIPE_MAX_NUMBER);
        this.updates = new WordCounter(PIPE_MAX_NUMBER, wordsNumber);
    }

    @Override
    protected boolean doAction() throws Exception {
        final Set<String> stopwords = new HashSet<>(Files.readAllLines(this.stopwordsFile));
        this.startAgents(stopwords);

        Files.list(this.filesDirectory)
                .filter(p -> p.toString().matches(".*pdf$"))
                .forEach(this.paths::enqueue);
        return false;
    }

    @Override
    protected void doEnd() {
        this.paths.close();
    }

    public List<TaskIterator.TaskType> agentTypes(){
        return Stream.generate(()->Arrays.asList(TaskIterator.TaskType.values())).flatMap(List::stream).limit(this.getTotalThreads()).collect(Collectors.toList());
    }

    private void startAgents(final Set<String> stopwords){
        this.agentTypes().forEach(type->{
            new FilterAgent(new TaskIterator(
                    type,
                    this.paths,
                    this.documents,
                    this.pages,
                    this.updates,
                    this.getSuspendedFlag(),
                    this.getTicketManager(),
                    stopwords
            ), this.view).start();
        });
        new GeneratorAgent(Collections.singletonList(new UpdateSinkTask(
                this.view,
                this.updates,
                this.getSuspendedFlag(),
                this.getTicketManager()
        )), this.view).start();
    }

    private int getTotalThreads(){
        return Runtime.getRuntime().availableProcessors() +1;
    }
}
