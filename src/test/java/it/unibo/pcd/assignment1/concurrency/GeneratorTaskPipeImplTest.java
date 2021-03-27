package it.unibo.pcd.assignment1.concurrency;

import it.unibo.pcd.assignment1.model.concurrency.GeneratorPipe;
import it.unibo.pcd.assignment1.model.concurrency.GeneratorPipeImpl;

public class GeneratorTaskPipeImplTest extends AbstractGeneratorTaskPipeTest {
    @Override
    protected GeneratorPipe<Integer> createIntegerQueue() {
        return new GeneratorPipeImpl<>();
    }
}
