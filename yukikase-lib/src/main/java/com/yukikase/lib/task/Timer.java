package com.yukikase.lib.task;

public abstract class Timer {

    private boolean isCancelled = false;

    public long getDelay() {
        return 0;
    }

    public long getInterval() {
        return 1;
    }

    public abstract void run();

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public final void cancel() {
        this.isCancelled = true;
    }
}
