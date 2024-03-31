package tutorial_000.languageNewFeatures;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class that allow us to simulate call to remote resources (like external API) for Virtual Threads tutorial. 
 */
public class VirtualThreadsTask implements Callable<Integer> {

	private final int number;
	
	public VirtualThreadsTask(int number) {
		this.number = number;
	}
	
	/**
	 * Method that simulate external service by waiting 1 second, then returning a random number.
	 */
	@Override
	public Integer call() {
		//System.out.printf("Thread %s - Task %d waiting...%n", Thread.currentThread().getName(), number);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.out.printf("Thread %s - Task %d canceled.%n", Thread.currentThread().getName(), number);
			return -1;
		}
	
		//System.out.printf("Thread %s - Task %d finished.%n", Thread.currentThread().getName(), number);
		
		return ThreadLocalRandom.current().nextInt(100);
	}
}
