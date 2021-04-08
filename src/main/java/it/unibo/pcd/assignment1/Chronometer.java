package it.unibo.pcd.assignment1;

/**
 * A chronometer capable of measuring intervals of time.
 */
public interface Chronometer {
    /**
     * It starts the chronometer.
     */
    void start();

    /**
     * It stops the chronometer, registering the interval of time passed between this instant and the start.
     */
    void stop();

    /**
     * It returns the last interval of time registered.
     * @return the last interval of time registered or 0 if none was registered before
     */
    long getTime();
}
