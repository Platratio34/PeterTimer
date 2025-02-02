package com.peter.petertimer;

/**
 * Timer callback interface
 */
public interface TimeRunnable {
	
	/**
	 * Callback from a Timer
	 * @param timer The timer that called the runnable
	 */
	public void run(AbstractTimer timer);
}
