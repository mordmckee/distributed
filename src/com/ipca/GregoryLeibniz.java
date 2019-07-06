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
