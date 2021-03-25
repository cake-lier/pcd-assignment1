package it.unibo.pcd.assignment1.controller.agents.impl;

import it.unibo.pcd.assignment1.controller.agents.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.model.pipes.Pipe;
import it.unibo.pcd.assignment1.model.pipes.PipeConnector;
import it.unibo.pcd.assignment1.model.pipes.impl.PipeConnectorImpl;
import it.unibo.pcd.assignment1.model.tasks.Task;
import it.unibo.pcd.assignment1.model.tasks.impl.DocumentFilterTask;
import it.unibo.pcd.assignment1.model.tasks.impl.PageFilterTask;
import it.unibo.pcd.assignment1.model.tasks.impl.PathFilterTask;
import it.unibo.pcd.assignment1.model.updates.Update;
import it.unibo.pcd.assignment1.wrapper.Document;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TaskIterator implements Iterator<Task> {
    private final List<Task> tasks;
    private int taskIndex;

    public TaskIterator(final InitialTask task,
                        final Pipe<Path> paths,
                        final Pipe<Document> documents,
                        final Pipe<String> pages,
                        final Pipe<Update> updates,
                        final AgentSuspendedFlag agentState,
                        final Set<String> stopwords) {
        this.tasks = Arrays.asList(new PathFilterTask(this.createPipeConnector(paths, documents), agentState),
                                   new DocumentFilterTask(this.createPipeConnector(documents, pages), agentState),
                                   new PageFilterTask(this.createPipeConnector(pages, updates), stopwords, agentState));
        this.taskIndex = task.getIndex();
    }

    @Override
    public boolean hasNext() {
        return this.taskIndex < 3;
    }

    @Override
    public Task next() {
        final Task task = this.tasks.get(this.taskIndex);
        this.taskIndex++;
        return task;
    }

    private <R, P> PipeConnector<R, P> createPipeConnector(final Pipe<R> resource, final Pipe<P> produce) {
        return new PipeConnectorImpl<>(resource, produce);
    }

    public enum InitialTask {
        PATH(0),
        DOCUMENT(1),
        PAGE(2);

        private final int index;

        InitialTask(final int index) {
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }
    }
}
