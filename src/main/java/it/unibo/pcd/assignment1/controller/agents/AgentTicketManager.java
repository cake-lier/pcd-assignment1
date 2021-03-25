package it.unibo.pcd.assignment1.controller.agents;

public interface AgentTicketManager {

    void incrementAgentOfType(Class<?> klass);

    boolean decrementAgentOfType(Class<?> klass);
}
