package it.unibo.pcd.assignment1.jpf.main;

import it.unibo.pcd.assignment1.jpf.controller.tasks.impl.UpdateSinkTask;
import it.unibo.pcd.assignment1.jpf.model.entities.impl.PageImpl;
import it.unibo.pcd.assignment1.jpf.model.tasks.impl.PageFilterTask;

public class PageMainTest extends JPFMainTest{
    private static final String WORD = "word";

    public static void main(String[] args) {
        insertDataAndClose(pagePipe,new PageImpl(WORD));
        createAgents(new PageFilterTask(pagePipe, wordCounter, taskCounter),new UpdateSinkTask(wordCounter,taskCounter));
    }
}
