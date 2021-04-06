package it.unibo.pcd.assignment1.jpf.main;

import it.unibo.pcd.assignment1.jpf.controller.agents.impl.AgentImpl;
import it.unibo.pcd.assignment1.jpf.controller.tasks.Task;
import it.unibo.pcd.assignment1.jpf.model.entities.Document;
import it.unibo.pcd.assignment1.jpf.model.entities.Page;
import it.unibo.pcd.assignment1.jpf.model.pipes.Pipe;
import it.unibo.pcd.assignment1.jpf.model.pipes.WordCounter;
import it.unibo.pcd.assignment1.jpf.model.pipes.impl.BoundedPipe;
import it.unibo.pcd.assignment1.jpf.model.pipes.impl.WordCounterImpl;
import it.unibo.pcd.assignment1.jpf.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.jpf.model.tasks.impl.TaskCounterImpl;

import java.util.Arrays;
import java.util.Collections;

public class JPFMainTest {
    private static final int PIPES_SIZE = 1000;
    private static final int WORD_NUMBER = 1;

    protected static final Pipe<String> pathPipe = new BoundedPipe<>(PIPES_SIZE);
    protected static final Pipe<Page> pagePipe = new BoundedPipe<>(PIPES_SIZE);
    protected static final Pipe<Document> documentPipe = new BoundedPipe<>(PIPES_SIZE);
    protected static final WordCounter wordCounter = new WordCounterImpl(PIPES_SIZE,WORD_NUMBER);
    protected static final TaskCounter taskCounter = new TaskCounterImpl();


    protected static void createAgents(final Task... tasks){
        Arrays.asList(tasks).forEach(task->new AgentImpl(Collections.singletonList(task)).go());
    }

    protected static <X> void insertDataAndClose(final Pipe<X> pipe,final X data){
        pipe.enqueue(data);
        pipe.close();
    }
}
