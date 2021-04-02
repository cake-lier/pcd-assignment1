package it.unibo.pcd.assignment1;

public class ChronometerImpl implements Chronometer {
	private static final String STOP_NOT_RUNNING_MSG = "You cannot stop a non-running chronometer";

	private boolean started;
	private long startTime;
	private long endTime;

	public ChronometerImpl() {
		this.startTime = 0;
		this.endTime = 0;
	}

	@Override
	public void start() {
		this.started = true;
		this.startTime = System.currentTimeMillis();
	}

	@Override
	public void stop() {
		if (!this.started) {
			throw new IllegalStateException(STOP_NOT_RUNNING_MSG);
		}
		this.endTime = System.currentTimeMillis();
	}

	@Override
	public long getTime() {
		return this.endTime - this.startTime;
	}
}
