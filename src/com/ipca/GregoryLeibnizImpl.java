package com.ipca;

import java.util.concurrent.Callable;

/**
 * The Class GregoryLeibnizImpl.
 */
public class GregoryLeibnizImpl implements Callable<Double> { 	
	
	/** The begin. */
	private long begin;
	
	/** The end. */
	private long end;
	/** The in circle. */
	private double factor;
	/** The sum. */
	private double sum;
	
	/**
	 * Instantiates a new gregory leibniz impl.
	 *
	 * @param begin the begin
	 * @param end the end
	 */
	public GregoryLeibnizImpl(long begin, long end) {
		this.begin = begin;
		this.end = end;
	}
	
	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	public Double call() {
		for(long k = begin; k < end; k++) {
			if(k % 2 == 0) factor = 1.0;
			else factor = -1.0;
									
			sum += factor / (2*k +1);
		}
		return sum;
	}
}
