package com.peter.petertimer;

/** Version of `TimeRunnable` that will **ONLY** be executed exactly on the tick it was requested, and will be skipped otherwise */
public interface TimeRunnableExact extends TimeRunnable {

}
