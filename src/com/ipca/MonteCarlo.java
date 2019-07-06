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
