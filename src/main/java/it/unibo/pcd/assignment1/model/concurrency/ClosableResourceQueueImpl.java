package it.unibo.pcd.assignment1.model.concurrency;
import java.util.Optional;

public class ClosableResourceQueueImpl<X>  extends ResourceQueueImpl<X> implements ClosableResourceQueue<X>{
    private static String EXCEPTION_MESSAGE = "It's not possible to add values to closed ClosableResourceQueue";
    private boolean isClosed;

    public ClosableResourceQueueImpl(){
        this.isClosed = false;
    }

    @Override
    public void add(X data) {
        if(isClosed){
            throw new IllegalStateException(EXCEPTION_MESSAGE);
        }
        super.add(data);
    }

    @Override
    public Optional<X> pop() {
        return this.isEmpty() && this.isClosed? Optional.empty(): super.pop();
    }

    @Override
    public void close(){
        this.isClosed = true;
    }
}

