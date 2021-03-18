package it.unibo.pcd.assignment1.model.concurrency;
import java.util.Optional;

public class CloseableResourceQueueImpl<X>  extends ResourceQueueImpl<X> implements CloseableResourceQueue<X>{
    private static final String EXCEPTION_MESSAGE = "It's not possible to add values to closed ClosableResourceQueue";

    private boolean isClosed;

    public CloseableResourceQueueImpl(){
        this.isClosed = false;
    }

    @Override
    protected Optional<X> doDequeue() {
        return this.isEmpty() && this.isClosed? Optional.empty(): super.doDequeue();
    }

    @Override
    protected void doEnqueue(X value) {
        if(isClosed){
            throw new IllegalStateException(EXCEPTION_MESSAGE);
        }
        super.doEnqueue(value);
    }

    @Override
    public void close(){
        this.isClosed = true;
    }
}

