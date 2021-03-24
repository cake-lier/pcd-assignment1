package it.unibo.pcd.assignment1.model.policy.impl;

import it.unibo.pcd.assignment1.model.agent.AgentFactory;
import it.unibo.pcd.assignment1.model.concurrency.pipe.Pipe;
import it.unibo.pcd.assignment1.model.concurrency.pipe.PipeConnector;
import it.unibo.pcd.assignment1.model.update.Update;
import it.unibo.pcd.assignment1.view.View;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ControllerFilterPolicy extends AbstractPipePolicy<Update, Path> {
    private static final int TYPES_OF_THREADS = 4;

    private final View view;
    private final Path filesDirectory;
    private final Path stopwordsFile;
    private final AgentFactory factory;

    public ControllerFilterPolicy(final View view, final Path filesDirectory, final Path stopwordsFile,
                                  final AgentFactory factory, final PipeConnector<Update,Path> connector) {
        super(connector);
        this.view = view;
        this.factory = factory;
        this.filesDirectory = filesDirectory;
        this.stopwordsFile = stopwordsFile;
    }

    @Override
    public void start() {
        this.filePaths(filesDirectory).forEach(this.getConnector()::writeInPipe);
        this.createAgents(factory,computeNumberOfThreads(),this.getStopwards(stopwordsFile));
        do {
            final Optional<Update> update = this.getConnector().readFromPipe();
            if (!update.isPresent()) {
                break;
            }
            this.view.displayProgress(update.get().getFrequencies(), update.get().getProcessedWords());
        } while (true);
        this.view.displayCompletion();
    }
    private Set<String> getStopwards(final Path path){
        try {
            return new HashSet<>(Files.readAllLines(path));
        } catch (IOException e) {
            e.printStackTrace();//TODO manage this
            System.exit(0);
            return null;
        }
    }

    private void createAgents(final AgentFactory agentFactory, final int threadNumber, final Set<String> stopwords){
        IntStream.range(0,threadNumber).forEach(e->{
            switch (e%TYPES_OF_THREADS){
                case 1:
                    agentFactory.pathAgent(stopwords).go();
                    break;
                case 2:
                    agentFactory.documentAgent(stopwords).go();
                    break;
                case 3:
                    agentFactory.pageAgent(stopwords).go();
                    break;
            }
        });
    }

    private Set<Path> filePaths(final Path filesDirectory){
        try {
            return Files.list(filesDirectory)
                    .filter(p -> p.toString().matches(".*pdf$"))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
            return null;
        }
    }

    private int computeNumberOfThreads(){
        return Runtime.getRuntime().availableProcessors();
    }

}
