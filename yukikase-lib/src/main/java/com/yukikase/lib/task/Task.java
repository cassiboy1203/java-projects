package com.yukikase.lib.task;

public abstract class Task {
    private boolean isCancelled = false;

    public long getDelay() {
        return 0;
    }

    public abstract void run();

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public final void cancel() {
        this.isCancelled = true;
    }
}
