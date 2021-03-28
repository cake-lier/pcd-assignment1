package it.unibo.pcd.assignment1.sequential;

public class Chronometer {
	private static final String STOP_NOT_RUNNING_MSG = "You cannot stop a non-running chronometer";

	private boolean started;
	private long startTime;
	private long endTime;

	public Chronometer() {
		this.startTime = 0;
		this.endTime = 0;
	}

	public void start() {
		this.started = true;
		this.startTime = System.currentTimeMillis();
	}

	public void stop() {
		if (!this.started) {
			throw new IllegalStateException(STOP_NOT_RUNNING_MSG);
		}
		this.endTime = System.currentTimeMillis();
	}

	public long getTime() {
		return this.endTime - this.startTime;
	}
}
