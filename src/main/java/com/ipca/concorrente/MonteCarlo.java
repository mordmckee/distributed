package com.ipca.concorrente;

import java.util.concurrent.ThreadLocalRandom;

import akka.actor.ActorSystem;


public class MonteCarlo {
	
	public static double monteCarloDistributed(long points) {
		
		
		
		long inCircle = 0;

		for (long i = 0; i < points; i++) {

			double x = ThreadLocalRandom.current().nextDouble();
			double y = ThreadLocalRandom.current().nextDouble();

			if(x * x + y * y <= 1) inCircle++;
		}

		return 4.0 * inCircle / points;
		
	}
}
