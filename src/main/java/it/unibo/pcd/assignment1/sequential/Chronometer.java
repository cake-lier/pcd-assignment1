package it.unibo.pcd.assignment1.sequential;

public class Chronometer {
	private long startTime;

	public Chronometer() {
		this.startTime = 0;
	}

	public void start() {
		this.startTime = System.currentTimeMillis();
	}

	public long stop() {
		return System.currentTimeMillis() - this.startTime;
	}
}
