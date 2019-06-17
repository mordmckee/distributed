package com.ipca;

import java.util.concurrent.Callable;

/**
 * The Class MonteCarloImpl.
 */
public class MonteCarloImpl implements Callable<Double> { 	
	
	/** The in circle. */
	private long inCircle = 0;
	
	/** The begin. */
	private long begin;
	
	/** The end. */
	private long end;
	
	/**
	 * Instantiates a new monte carlo impl.
	 *
	 * @param begin the begin
	 * @param end the end
	 */
	public MonteCarloImpl(long begin, long end) {
		this.begin = begin;
		this.end = end;
	}
	
	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Double call() throws Exception {
		for(long k = begin; k < end; k++) {
			double x = Math.random();
			double y = Math.random();
	
			if(x * x + y * y <= 1) inCircle++;
		}
		return 4.0 * inCircle;
	}
}