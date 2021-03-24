package it.unibo.pcd.assignment1.model.agent.impl;

import it.unibo.pcd.assignment1.model.agent.Agent;
import it.unibo.pcd.assignment1.model.agent.AgentFactory;
import it.unibo.pcd.assignment1.model.agent.SharedAgentState;
import it.unibo.pcd.assignment1.model.concurrency.BoundedPipe;
import it.unibo.pcd.assignment1.model.concurrency.Pipe;
import it.unibo.pcd.assignment1.model.concurrency.WordCounter;
import it.unibo.pcd.assignment1.model.policy.Policy;
import it.unibo.pcd.assignment1.model.policy.impl.ControllerFilterPolicy;
import it.unibo.pcd.assignment1.model.policy.impl.DocumentFilterPolicy;
import it.unibo.pcd.assignment1.model.policy.impl.PageFilterPolicy;
import it.unibo.pcd.assignment1.model.policy.impl.PathFilterPolicy;
import it.unibo.pcd.assignment1.model.update.Update;
import it.unibo.pcd.assignment1.view.View;
import it.unibo.pcd.assignment1.wrapper.Document;
import it.unibo.pcd.assignment1.wrapper.Page;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class AgentFactoryImpl implements AgentFactory {
    private static final int PIPE_MAX_NUMBER = 1000;

    private final Pipe<Path> paths;
    private final Pipe<Document> documents;
    private final Pipe<Page> pages;
    private final Pipe<Update> updates;
    private final SharedAgentState agentState;

    public AgentFactoryImpl(final SharedAgentState agentState, final int wordsNumber) {
        //TODO pipe connector
        this.paths = new BoundedPipe<>(PIPE_MAX_NUMBER);
        this.documents = new BoundedPipe<>(PIPE_MAX_NUMBER);
        this.pages = new BoundedPipe<>(PIPE_MAX_NUMBER);
        this.updates = new WordCounter(PIPE_MAX_NUMBER,wordsNumber);
        this.agentState = agentState;
    }

    public Agent pathAgent(final Set<String> stopwords) {
        return createAgent(Arrays.asList(pathPolicy(),documentPolicy(),pagePolicy(stopwords)));
    }

    public Agent documentAgent(final Set<String> stopwords) {
        return createAgent(Arrays.asList(documentPolicy(),pagePolicy(stopwords)));
    }

    public Agent pageAgent(final Set<String> stopwords) {
        return createAgent(singlePolicy(pagePolicy(stopwords)));
    }

    public Agent controllerAgent(final View view,final Path directories,final Path stopwards){
        return createAgent(singlePolicy(controllerPolicy(view,directories,stopwards)));
    }

    private Agent createAgent(final List<Policy> policies){
        return new GenericAgent(policies);
    }
    private List<Policy> singlePolicy(final Policy policy){ return Collections.singletonList(policy);}

    private  Policy pathPolicy(){return new PathFilterPolicy(paths,documents,agentState);}
    private Policy documentPolicy(){return new DocumentFilterPolicy(documents,pages,agentState);}
    private Policy pagePolicy(final Set<String> stopWords){return new PageFilterPolicy(pages,updates,stopWords,agentState); }
    private Policy controllerPolicy(final View view,final Path directories,final Path stopwards){
        return new ControllerFilterPolicy(view,directories,stopwards,this,paths,updates);
    }
}