package languageNewFeatures;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class _004_VirtualThreads {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		/*
		 * Java 19 is released with a preview feature called "Virtual Threads". Let see what it is.
		 */

		/*
		 * WHAT SORTS OF PROBLEMS VIRTUAL THREADS TREATS ?
		 *
		 * When we work on a backend application under heavy load, threads creation and use is often a bottleneck
		 * of such a use case. For every incoming request, a thread is needed to process this request. One Java
		 * thread corresponds to one operating system thread, and those are resource-hungry. We should not start more
		 * than a few hundred, otherwise we risk the stability of the entire system.
		 *
		 * However, a few hundred are not always enough, especially if it takes long time to process a request because
		 * of the need to wait for blocking data structures such as queues, locks, or external services like databases,
		 * microservices, or cloud APIs.
		 *
		 * For example, if a request takes two seconds and we limit the thread pool to 100 threads, then a maximum of
		 * 50 requests per second could be answered. However, the CPU would be far from being utilized since it would
		 * spend most of its time waiting for responses from the external services, even if several threads are served
		 * per CPU core.
		 *
		 * Until today we have only been able to overcome this problem with reactive programming by using frameworks
		 * like RxJava. However, reactive code may be many times more complex than sequential code and hard to maintain.
		 * An example of reactive programming would be :
		 *

			public DeferredResult<ResponseEntity<?>> createOrder(CreateOrderRequest createOrderRequest, Long sessionId, HttpServletRequest context) {
				DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

				Observable.just(createOrderRequest)
					.doOnNext(this::validateRequest)
					.flatMap(
						request ->
							sessionService.getSessionContainer(request.getClientId(), sessionId)
								.toObservable()
								.map(ResponseEntity::getBody)
					)
					.map(
						sessionContainer ->
							enrichCreateOrderRequest(createOrderRequest, sessionContainer, context)
					)
					.flatMap(
						enrichedRequest ->
							orderPersistenceService.persistOrder(enrichedRequest).toObservable()
					)
					.subscribeOn(Schedulers.io())
					.subscribe(
						success -> deferredResult.setResult(ResponseEntity.noContent()),
						error -> deferredResult.setErrorResult(error)
					);

				return deferredResult;
			}

		 *
		 * As we can see, this code is hardly readable, but it is also extremely difficult to debug. For example,
		 * it would make no sense to set a breakpoint here because the code only defines the reactive flow but does
		 * not execute it. The code is executed only after the call to subscribe() (at the end of the method) by the
		 * reactive library in a separate thread pool.
		 *
		 * In addition, the database drivers and drivers for other external services must also support the reactive
		 * model.
		 */

		/*
		 * WHAT ARE VIRTUAL THREADS ?
		 *
		 * Virtual threads solve the previously described problem in a way that allows us to write easily readable and
		 * maintainable code. Virtual threads feel like normal threads from a Java code perspective, but they are not
		 * mapped 1:1 to operating system threads.
		 *
		 * Instead, there is a pool of so-called carrier threads onto which a virtual thread is temporarily mapped. As
		 * soon as the virtual thread encounters a blocking operation, the virtual thread is removed from the carrier
		 * thread, and the carrier thread can execute another virtual thread (a new one or a previously blocked one).
		 *
		 * We may watch "src/virtual-threads.png" file which contains a draw of this behavior.
		 *
		 * Blocking operations thus no longer block the executing thread. This allows us to process a large number of
		 * requests in parallel with a small pool of carrier threads.
		 *
		 * We now could replace the above code example which use reactive programming framework by something way more
		 * simple :
		 *

			public void createOrder(CreateOrderRequest createOrderRequest, Long sessionId, HttpServletRequest context) {
				validateRequest(createOrderRequest);

				SessionContainer sessionContainer =
					sessionService.getSessionContainer(createOrderRequest.getClientId(), sessionId)
						.execute()
						.getBody();

				EnrichedCreateOrderRequest enrichedCreateOrderRequest = enrichCreateOrderRequest(createOrderRequest, sessionContainer, context);

				orderPersistenceService.persistOrder(enrichedCreateOrderRequest);
			}

		 *
		 * This code is not only easier to write and read, but we also may debug it by conventional means,like any sequential code.
		 */

		/*
		 * VIRTUAL THREAD FUNCTIONNING EXAMPLE
		 *
		 * We will see now the point of virtual threads with a proper code example. For this purpose, we will firstly
		 * call a method from the VirtualThreadsTask we created for simulating external service, that took a given
		 * time to answer us. This will show us the time it takes to process 1000 tasks for 100 platform threads (it is
		 * how non-virtual threads are referred to).
		 *
		 */

		// Here we create a pool of 100 threads that will treat our tasks.
		ExecutorService executor = Executors.newFixedThreadPool(100);

		List<VirtualThreadsTask> tasks = new ArrayList<>();

		// Here we create the 1000 tasks to process.
		for (int i = 0; i < 1_000; i++) {
			tasks.add(new VirtualThreadsTask(i));
		}

		long time = System.currentTimeMillis();

		// Here we launch execution of the 1000 tasks.
		List<Future<Integer>> futures = executor.invokeAll(tasks);

		long sum = 0;

		// Here we treat the results of the tasks.
		for (Future<Integer> future : futures) {
		  sum += future.get();
		}

		time = System.currentTimeMillis() - time;

		// Finally, we display the result and execution time for classical threads.
		System.out.println("Sum of integers returned by all tasks (with classical threads) = " + sum + "; Needed time to execute the 1000 tasks (with classical threads) = " + time + " ms");

		executor.shutdown();

		/*
		 * As expected, 100 threads treating 1000 tasks, with each task waiting 1 second, take approximately 10 seconds. Indeed,
		 * each classical thread process the tasks sequentially. It launch the first one, wait it's completion, then launch the
		 * second and so on.
		 *
		 * We will now execute the same code, but we will use virtual threads instead of classical threads. And to see the
		 * power of virtual threads, we will not execute it on 1000 tasks, nor 10 000 tasks, but on 100 000 tasks. Then we will
		 * be able to compare execution times.
		 */

		// Here to creat our virtual threads, we use newVirtualThreadPerTaskExecutor() instead of newFixedThreadPool().
		// This executor does not use a thread pool but creates a new virtual thread for each task.
		ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();


		tasks = new ArrayList<>();

		// Here we create the 100_000 tasks to process.
		for (int i = 0; i < 100_000; i++) {
			tasks.add(new VirtualThreadsTask(i));
		}

		time = System.currentTimeMillis();

		// Here we launch execution of the 1000 tasks.
		futures = virtualExecutor.invokeAll(tasks);

		sum = 0;

		// Here we treat the results of the tasks.
		for (Future<Integer> future : futures) {
		  sum += future.get();
		}

		time = System.currentTimeMillis() - time;

		// Finally, we display the result and execution time for classical threads.
		System.out.println("Sum of integers returned by all tasks (with virtual threads) = " + sum + "; Needed time to execute the 1000 tasks (with virtual threads) = " + time + " ms");

		executor.shutdown();

		/*
		 * We now see the power and usefulness of virtual threads. When it took 10 seconds for classical threads to process only
		 * 1000 tasks, it took approximately 6 seconds for virtual threads to treat 100 000 tasks. For comparison, it would take
		 * a more than 16 minutes for our classical threads to treat this amount of tasks.
		 */

		/*
		 * CREATING VIRTUAL THREAD.
		 *
		 * In previous example, we saw that Executors.newVirtualThreadPerTaskExecutor() method allow us to create one virtual
		 * thread per task.
		 *
		 * We will note that two usefull methods are now provided by Thread class and allow us to explicitly start virtual threads :
		 *   - Thread.startVirtualThread(() -> { //code to run in thread });
		 *   - Thread.ofVirtual().start(() -> { // code to run in thread });
		 *
		 * The second method, Thread.ofVirtual(), returns a VirtualThreadBuilder whose start() method starts a virtual thread.
		 * The alternative method Thread.ofPlatform() returns a PlatformThreadBuilder via which we can start a platform thread (non
		 * virtual threads)..
		 *
		 * Both builders implement the Thread.Builder interface. This allows us to write flexible code that decides at runtime
		 * whether it should run in a virtual or in a platform thread :
		 *
		 	Thread.Builder threadBuilder = createThreadBuilder();
			threadBuilder.start(() -> {
				// code to run in thread
			});
		 *
		 * Finally, we will note that we can find out if code is running in a virtual thread with Thread.currentThread().isVirtual().
		 */
	}

}
