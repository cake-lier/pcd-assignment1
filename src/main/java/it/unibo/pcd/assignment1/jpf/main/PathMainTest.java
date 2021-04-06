package it.unibo.pcd.assignment1.jpf.main;

import it.unibo.pcd.assignment1.jpf.model.entities.impl.DocumentImpl;
import it.unibo.pcd.assignment1.jpf.model.entities.impl.PageImpl;
import it.unibo.pcd.assignment1.jpf.model.tasks.impl.DocumentFilterTask;
import it.unibo.pcd.assignment1.jpf.model.tasks.impl.PageFilterTask;
import it.unibo.pcd.assignment1.jpf.model.tasks.impl.PathFilterTask;

import java.util.Collections;

public class PathMainTest extends JPFMainTest{
    private static String FILE_NAME = "file";

    public static void main(String[] args) {
        insertDataAndClose(pathPipe,FILE_NAME);
        createAgents(new PathFilterTask(pathPipe,documentPipe,taskCounter),new DocumentFilterTask(documentPipe,pagePipe,taskCounter));
    }
}
