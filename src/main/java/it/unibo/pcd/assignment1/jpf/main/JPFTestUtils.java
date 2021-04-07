package it.unibo.pcd.assignment1.jpf.main;

import it.unibo.pcd.assignment1.jpf.controller.agents.impl.AgentImpl;
import it.unibo.pcd.assignment1.jpf.controller.tasks.Task;
import it.unibo.pcd.assignment1.jpf.model.pipes.Pipe;

import java.util.Arrays;
import java.util.Collections;

public class JPFTestUtils {
    private JPFTestUtils() {}

    public static void createAgents(final Task... tasks){
        Arrays.asList(tasks).forEach(task -> new AgentImpl(Collections.singletonList(task)).go());
    }

    public static <E> void insertDataAndClose(final Pipe<E> pipe, final E data){
        pipe.enqueue(data);
        pipe.close();
    }
}
