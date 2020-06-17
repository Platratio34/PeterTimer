package peterTimer;

public interface TimeRunnable {
	
	/**
	 * callback from a Timer
	 * @param timer - The timer that called the runnable
	 */
	public void run(Timer timer);
  
}
