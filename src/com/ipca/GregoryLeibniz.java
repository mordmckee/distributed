package com.ipca;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * The Class GregoryLeibniz.
 */
public class GregoryLeibniz {
	
	/** The points. */
	private long points;
	
	/** The n processors. */
	private int nProcessors;
	
	/**
	 * Instantiates a new gregory leibniz.
	 */
	public GregoryLeibniz() { }
	
	/**
	 * Instantiates a new gregory leibniz.
	 *
	 * @param points the points
	 * @param nProcessors the n processors
	 */
	public GregoryLeibniz(long points, int nProcessors) {
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
		
		ExecutorService executor = Executors.newFixedThreadPool(nProcessors);
		
		for (long k = 0; k < points; k += points / nProcessors) {
			Future<Double> t = executor.submit(new GregoryLeibnizImpl(k, k + points / nProcessors));
			sum += t.get().doubleValue();
		}
							
		executor.shutdown();
		
		return 4.0 * sum;
	}
	
	/**
	 * Gregory leibniz.
	 *
	 * @param numberTotalGenerated
	 *            the number total generated
	 * @return the double
	 */
	public static double gregoryLeibnizSequencialMethod(double numberTotalGenerated) {
		double factor = 1.0;
		double sum = 0.0;

		for (long k = 0; k < numberTotalGenerated; k++) {
			sum += factor / (2 * k + 1);
			factor = -factor;
		}
		return 4.0 * sum;
	}
}
