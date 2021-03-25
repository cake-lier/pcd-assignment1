package it.unibo.pcd.assignment1.controller.impl;

import it.unibo.pcd.assignment1.controller.Controller;
import it.unibo.pcd.assignment1.controller.agents.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.controller.agents.impl.AgentSuspendedFlagImpl;
import it.unibo.pcd.assignment1.controller.agents.impl.GeneratorSinkAgent;
import it.unibo.pcd.assignment1.model.tasks.impl.ControllerTask;
import it.unibo.pcd.assignment1.view.View;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Objects;

public class ControllerImpl implements Controller {
    private final View view;
    private final AgentSuspendedFlag suspendedFlag;

    public ControllerImpl(final View view) {
        this.view = Objects.requireNonNull(view);
        this.suspendedFlag = new AgentSuspendedFlagImpl();
    }

    @Override
    public void launch(final Path filesDirectory, final Path stopwordsFile, final int wordsNumber) {
        new GeneratorSinkAgent(Collections.singletonList(new ControllerTask(
            this.view,
            filesDirectory,
            stopwordsFile,
            wordsNumber,
            this.suspendedFlag
        )), this.view).start();
    }

    @Override
    public void suspend() {
        this.suspendedFlag.setSuspended();
    }

    @Override
    public void resume() {
        this.suspendedFlag.setRunning();
    }

    @Override
    public void exit() {
        System.exit(0);
    }
}
