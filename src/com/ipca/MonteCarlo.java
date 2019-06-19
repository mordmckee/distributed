package com.ipca;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The Class MonteCarlo.
 */
public class MonteCarlo {
		
	/**
	 * Monte carlo method concorrent method.
	 *
	 * @param points the points
	 * @param nProcessors the n processors
	 * @return the double
	 * @throws InterruptedException the interrupted exception
	 * @throws ExecutionException the execution exception
	 */
	public static double monteCarloMethodConcorrentMethod(long points, int nProcessors) throws InterruptedException, ExecutionException {
		
		double sum = 0;
		
		List<Future<Double>> tasks = new ArrayList<>();
		
		ExecutorService executor = Executors.newFixedThreadPool(nProcessors);
		
		for (long i = 0; i < points; i += points / nProcessors) {
			Future<Double> task = executor.submit(() -> {
				long inCircle = 0;
				double val = 0;
				
				for(long k = 0; k < points / nProcessors; k++) {
					double x = ThreadLocalRandom.current().nextDouble();
					double y = ThreadLocalRandom.current().nextDouble();
			
					if(x * x + y * y <= 1) inCircle++;
				}
				val = 4.0 * inCircle;
				
				return val;
			});
			
			tasks.add(task);
		}
		
		long pending = nProcessors;
		while(pending > 0) {
			for(Future<Double> future : tasks) {
				if(future.isDone() && pending > 0) {
					sum += future.get();
					pending--;
				}
			}
		}		

		executor.shutdown();
			
		return sum / points;
	}

	/**
	 * Monte carlo method sequencial method.
	 *
	 * @param points the points
	 * @return the double
	 */
	public static double monteCarloMethodSequencialMethod(long points) {
		long inCircle = 0;

		for (long i = 0; i < points; i++) {

			double x = ThreadLocalRandom.current().nextDouble();
			double y = ThreadLocalRandom.current().nextDouble();

			if(x * x + y * y <= 1) inCircle++;
		}

		return 4.0 * inCircle / points;
	}
}
