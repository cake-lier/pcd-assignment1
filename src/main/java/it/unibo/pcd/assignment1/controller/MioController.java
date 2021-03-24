package it.unibo.pcd.assignment1.controller;

import it.unibo.pcd.assignment1.model.agent.SharedAgentState;
import it.unibo.pcd.assignment1.model.agent.SharedAgentStateImpl;
import it.unibo.pcd.assignment1.model.agent.impl.AgentFactoryImpl;
import it.unibo.pcd.assignment1.view.View;
import java.nio.file.Path;


public class MioController implements Controller{

    private final View view;
    private final SharedAgentState agentState;

    public MioController(final View view) {
        this.view = view;
        view.show();
        this.agentState = new SharedAgentStateImpl();
    }

    @Override
    public void launch(final Path filesDirectory,final Path stopwordsFile,final int wordsNumber) {
        new AgentFactoryImpl(this.agentState,wordsNumber).controllerAgent(this.view,filesDirectory,stopwordsFile).go();
    }

    @Override
    public void suspend() {
        this.agentState.setSuspendedState();
    }

    @Override
    public void resume() {
        this.agentState.setRunningState();
    }

    @Override
    public void exit() {
        System.exit(0);
    }
}
