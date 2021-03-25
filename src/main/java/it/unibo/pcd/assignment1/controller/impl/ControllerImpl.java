package it.unibo.pcd.assignment1.controller.impl;

import it.unibo.pcd.assignment1.controller.Controller;
import it.unibo.pcd.assignment1.controller.agents.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.controller.agents.AgentTicketManager;
import it.unibo.pcd.assignment1.controller.agents.impl.AgentSuspendedFlagImpl;
import it.unibo.pcd.assignment1.controller.agents.impl.AgentTicketManagerImpl;
import it.unibo.pcd.assignment1.controller.agents.impl.GeneratorAgent;
import it.unibo.pcd.assignment1.model.tasks.impl.ControllerTask;
import it.unibo.pcd.assignment1.view.View;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Objects;

public class ControllerImpl implements Controller {
    private final View view;
    private final AgentSuspendedFlag suspendedFlag;
    private final AgentTicketManager ticketManager;

    public ControllerImpl(final View view) {
        this.view = Objects.requireNonNull(view);
        this.suspendedFlag = new AgentSuspendedFlagImpl();
        this.ticketManager = new AgentTicketManagerImpl();
    }

    @Override
    public void launch(final Path filesDirectory, final Path stopwordsFile, final int wordsNumber) {
        new GeneratorAgent(Collections.singletonList(new ControllerTask(
            this.view,
            filesDirectory,
            stopwordsFile,
            wordsNumber,
            this.suspendedFlag,
            this.ticketManager
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
