package it.unibo.pcd.assignment1.jpf.model.tasks;

/**
 * The types of "filter" {@link it.unibo.pcd.assignment1.concurrent.controller.tasks.Task} which are allowed into this
 * application.
 */
public enum FilterTaskTypes {
    /**
     * The {@link it.unibo.pcd.assignment1.concurrent.controller.tasks.Task} which transforms {@link java.nio.file.Path}s into
     * {@link it.unibo.pcd.assignment1.concurrent.model.entities.Document}s.
     */
    PATH,
    /**
     * The {@link it.unibo.pcd.assignment1.concurrent.controller.tasks.Task} which transforms
     * {@link it.unibo.pcd.assignment1.concurrent.model.entities.Document}s into
     * {@link it.unibo.pcd.assignment1.concurrent.model.entities.Page}s.
     */
    DOCUMENT,
    /**
     * The {@link it.unibo.pcd.assignment1.concurrent.controller.tasks.Task} which transforms
     * {@link it.unibo.pcd.assignment1.concurrent.model.entities.Page}s into
     * {@link it.unibo.pcd.assignment1.concurrent.model.entities.Update}s.
     */
    PAGE
}
