package com.ipca;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Class MonteCarlo.
 */
public class MonteCarlo {
		
	/** The points. */
	private long points;
	
	/** The n processors. */
	private int nProcessors = 1;
	
	/**
	 * Instantiates a new monte carlo.
	 *
	 * @param points the points
	 * @param nProcessors the n processors
	 */
	public MonteCarlo(long points, int nProcessors) {
		super();
		this.points = points;
		this.nProcessors = nProcessors;
	}

	
	/**
	 * Calculate PI.
	 *
	 * @return the double
	 * @throws InterruptedException the interrupted exception
	 * @throws ExecutionException the execution exception
	 */
	public double calculatePI() throws InterruptedException, ExecutionException {
		double sum = 0;
		
		ExecutorService executor = Executors.newWorkStealingPool(nProcessors);
		
		for (long k = 0; k < points; k += points / nProcessors) {
			Future<Double> t = executor.submit(new MonteCarloImpl(k, k + points / nProcessors));
			sum += t.get().doubleValue();
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
		int inCircle = 0;

		for (int i = 0; i < points; i++) {

			double x = Math.random();
			double y = Math.random();

			if(x * x + y * y <= 1) inCircle++;
		}

		return 4.0 * inCircle / points;
	}
}
