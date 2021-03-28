package it.unibo.pcd.assignment1.concurrent.model.shared.impl;

import it.unibo.pcd.assignment1.concurrent.model.shared.StopwordsSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class StopwordsSetImpl implements StopwordsSet {
    private final Set<String> stopwords;
    private final ReentrantLock lock;
    private final Condition areSet;
    private boolean stopwordsAreSet;

    public StopwordsSetImpl() {
        this.stopwords = new HashSet<>();
        this.lock = new ReentrantLock();
        this.stopwordsAreSet = false;
        this.areSet = this.lock.newCondition();
    }

    @Override
    public void set(final Collection<String> stopwords) {
        this.lock.lock();
        try {
            if (this.stopwordsAreSet) {
                throw new IllegalStateException("Stopwords cannot be reset");
            }
            this.stopwords.addAll(stopwords);
            this.stopwordsAreSet = true;
            this.areSet.signalAll();
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public Set<String> get() {
        this.lock.lock();
        try {
            while (!this.stopwordsAreSet) {
                try {
                    this.areSet.await();
                } catch (InterruptedException ignored) {}
            }
            return this.stopwords;
        } finally {
            this.lock.unlock();
        }
    }
}
