package it.unibo.pcd.assignment1.jpf.main;

import it.unibo.pcd.assignment1.jpf.controller.tasks.impl.UpdateSinkTask;
import it.unibo.pcd.assignment1.jpf.model.entities.impl.DocumentImpl;
import it.unibo.pcd.assignment1.jpf.model.entities.impl.PageImpl;
import it.unibo.pcd.assignment1.jpf.model.tasks.impl.DocumentFilterTask;
import it.unibo.pcd.assignment1.jpf.model.tasks.impl.PageFilterTask;

import java.util.Collections;

public class DocumentMainTest extends JPFMainTest{
    private static final String WORD = "word";

    public static void main(String[] args) {
        insertDataAndClose(documentPipe,new DocumentImpl(Collections.singletonList(new PageImpl(WORD))));
        createAgents(new DocumentFilterTask(documentPipe, pagePipe, taskCounter),new PageFilterTask(pagePipe,wordCounter,taskCounter));
    }
}
