package it.unibo.pcd.assignment1.controller.agents.impl;

import it.unibo.pcd.assignment1.controller.agents.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.controller.agents.AgentTicketManager;
import it.unibo.pcd.assignment1.model.pipes.Pipe;
import it.unibo.pcd.assignment1.model.pipes.PipeConnector;
import it.unibo.pcd.assignment1.model.pipes.impl.PipeConnectorImpl;
import it.unibo.pcd.assignment1.model.tasks.Task;
import it.unibo.pcd.assignment1.model.tasks.impl.DocumentFilterTask;
import it.unibo.pcd.assignment1.model.tasks.impl.PageFilterTask;
import it.unibo.pcd.assignment1.model.tasks.impl.PathFilterTask;
import it.unibo.pcd.assignment1.model.tasks.impl.UpdateSinkTask;
import it.unibo.pcd.assignment1.model.updates.Update;
import it.unibo.pcd.assignment1.view.View;
import it.unibo.pcd.assignment1.wrapper.Document;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TaskIterator implements Iterator<Task> {
    private final List<Task> tasks;
    private int taskIndex;

    public TaskIterator(final TaskType task,
                        final Pipe<Path> paths,
                        final Pipe<Document> documents,
                        final Pipe<String> pages,
                        final Pipe<Update> updates,
                        final AgentSuspendedFlag agentState,
                        final AgentTicketManager ticketManager,
                        final Set<String> stopwords) {
        this.tasks = Arrays.asList(new PathFilterTask(this.createPipeConnector(paths, documents), agentState,ticketManager),
                                   new DocumentFilterTask(this.createPipeConnector(documents, pages), agentState,ticketManager),
                                   new PageFilterTask(this.createPipeConnector(pages, updates), stopwords, agentState,ticketManager));
        this.taskIndex = task.getIndex();
    }

    @Override
    public boolean hasNext() {
        return this.taskIndex < TaskType.values().length;
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

    public enum TaskType {
        PATH(0),
        DOCUMENT(1),
        PAGE(2);

        private final int index;

        TaskType(final int index) {
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }
    }
}
