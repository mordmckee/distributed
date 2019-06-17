package com.ipca;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Class MonteCarlo.
 */
public class MonteCarlo {
	
	/** The in circle. */
	private AtomicInteger inCircle = new AtomicInteger(0);
	
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
	 * The Class MonteCarloImpl.
	 */
	public class MonteCarloImpl implements Runnable { 	
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			double x = Math.random();
			double y = Math.random();

			if(x * x + y * y <= 1) inCircle.incrementAndGet();
		}
	}
	
	/**
	 * Calculate PI.
	 *
	 * @return the double
	 */
	public double calculatePI() {
		ExecutorService executor = Executors.newWorkStealingPool(nProcessors);
		
		for(long i = 0; i < points; i++) {
			Runnable worker = new MonteCarloImpl();
			executor.execute(worker);
		}
		executor.shutdown();
		
		while(!executor.isTerminated()) { }
		
		return 4.0 * inCircle.get() / points;
	}
}
