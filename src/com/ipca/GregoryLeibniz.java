package com.ipca;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * The Class GregoryLeibniz.
 */
public class GregoryLeibniz {
	
	/**
	 * Gregory leibniz concorrent method.
	 *
	 * @param points the points
	 * @param nProcessors the n processors
	 * @return the double
	 * @throws InterruptedException the interrupted exception
	 * @throws ExecutionException the execution exception
	 */
	public static double gregoryLeibnizConcorrentMethod(long points, int nProcessors) throws InterruptedException, ExecutionException {
		
		double sum = 0;
				
		List<Future<Double>> tasks = new ArrayList<>();
		
		ExecutorService executor = Executors.newFixedThreadPool(nProcessors);
		
		for (long i = 0; i < points; i += points / nProcessors) {
			Future<Double> task = executor.submit(() -> {
				
				double val = 0, factor;
				
				for(long k = 0; k < points / nProcessors; k++) {
					if(k % 2 == 0) factor = 1.0;
					else factor = -1.0;
											
					val += factor / (2*k +1);
				}
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
		
		return (4 * sum) / nProcessors;
	}
	
	/**
	 * Gregory leibniz sequencial method.
	 *
	 * @param numberTotalGenerated the number total generated
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
