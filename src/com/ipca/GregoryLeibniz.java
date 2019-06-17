package com.ipca;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// TODO: Auto-generated Javadoc

/**
 * The Class GregoryLeibniz.
 */
public class GregoryLeibniz {
	
	/** The in circle. */
	private double factor;
	
	/** The sum. */
	private double sum;
	
	/** The points. */
	private long points;
	
	/** The n processors. */
	private int nProcessors;
	

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
	 * The Class GregoryLeibnizImpl.
	 */
	public class GregoryLeibnizImpl implements Runnable { 	
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			for (int k = 0; k < points; k++) {
				if(k % 2 == 0) factor = 1.0;
				else factor = -1.0;
								
				sum += factor / (2*k +1);
			}
		}
	}
	
	/**
	 * Calculate PI.
	 *
	 * @return the double
	 */
	public double calculatePI() {
		ExecutorService executor = Executors.newWorkStealingPool(nProcessors);
		
		Runnable worker = new GregoryLeibnizImpl();
		executor.execute(worker);
		
		executor.shutdown();
		
		while(!executor.isTerminated()) { }
		
		return 4.0 * sum;
	}
}
