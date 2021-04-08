package it.unibo.pcd.assignment1;

/**
 * An implementation of the Chronometer interface.
 */
public class ChronometerImpl implements Chronometer {
	private static final String STOP_NOT_RUNNING_MSG = "You cannot stop a non-running chronometer";

	private boolean started;
	private long startTime;
	private long endTime;

	/**
	 * Default constructor.
	 */
	public ChronometerImpl() {
		this.startTime = 0;
		this.endTime = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() {
		this.started = true;
		this.startTime = System.currentTimeMillis();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop() {
		if (!this.started) {
			throw new IllegalStateException(STOP_NOT_RUNNING_MSG);
		}
		this.endTime = System.currentTimeMillis();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getTime() {
		return this.endTime - this.startTime;
	}
}
