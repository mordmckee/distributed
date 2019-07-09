package com.ipca.distributed;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.CountDownLatch;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.routing.RoundRobinPool;
	
/**
 * The Class MonteCarlo.
 */
public class MonteCarlo {
		
	/** The latch. */
	static volatile CountDownLatch latch;
		

	/**
	 * Monte carlo distribuited method.
	 *
	 * @param iterations the iterations
	 * @param concurrentNr the concurrent nr
	 * @throws InterruptedException the interrupted exception
	 */
	public void monteCarloDistribuitedMethod(long iterations, int concurrentNr) throws InterruptedException {
		
		MonteCarlo monteCarlo = new MonteCarlo();
		
        for (int nActors = 1; nActors <= concurrentNr; nActors++) {
            latch = new CountDownLatch(1);
            monteCarlo.actorPI(nActors, iterations, concurrentNr);
            latch.await();
        }
	}
	
	/**
	 * Actor PI.
	 *
	 * @param nrOfWorkers the nr of workers
	 * @param iterations the iterations
	 * @param nrOfMessages the nr of messages
	 */
	public void actorPI(final long nrOfWorkers, final long iterations, final long nrOfMessages) {
		  
        ActorSystem system = ActorSystem.create("PiSystem");

        ActorRef listener = system.actorOf(Props.create(Listener.class), "listener");

        ActorRef master = system.actorOf(Props.create(Master.class, nrOfWorkers, iterations, listener), "master");

        master.tell(new CalculatePI(), master);
	  }
	
	/**
	 * The Class CalculatePI.
	 */
	static class CalculatePI { }
	
	/**
	 * The Class Work.
	 */
	private static class Work {
        
        /** The begin. */
        private long begin;
        
        /** The iterations. */
        private long iterations;

        /**
         * Instantiates a new work.
         *
         * @param begin the begin
         * @param iterations the iterations
         */
        public Work(long iterations) {
            this.iterations = iterations;
        }

        /**
         * Gets the iterations.
         *
         * @return the iterations
         */
        public long getiterations() {
            return iterations;
        }
    }

	/**
	 * The Class Result.
	 */
	private static class Result {
        
        /** The pi. */
        private double pi;

        /**
         * Instantiates a new result.
         *
         * @param pi the pi
         */
        public Result(double pi) {
            this.pi = pi;
        }

        /**
         * Gets the pi.
         *
         * @return the pi
         */
        public double getPI() {
            return pi;
        }
    }

    /**
     * The Class PiApproximation.
     */
    static class PiApproximation {
        
        /** The pi. */
        private double pi;

        /**
         * Instantiates a new pi approximation.
         *
         * @param pi the pi
         */
        public PiApproximation(double pi) {
            this.pi = pi;
        }

        /**
         * Gets the pi.
         *
         * @return the pi
         */
        public double getPi() {
            return pi;
        }
    }

    /**
     * The Class Worker.
     */
    public static class Worker extends UntypedAbstractActor {
    	
        /**
         * Calculate pi for.
         *
         * @param begin the begin
         * @param iterations the iterations
         * @return the double
         */
        private double calculatePiFor(long iterations) {
        	long inCircle = 0;

    		for (long i = 0; i < iterations; i++) {

    			double x = ThreadLocalRandom.current().nextDouble();
    			double y = ThreadLocalRandom.current().nextDouble();

    			if(x * x + y * y <= 1) inCircle++;
    		}

    		return 4.0 * inCircle / iterations;
        }

        /* (non-Javadoc)
         * @see akka.actor.UntypedAbstractActor#onReceive(java.lang.Object)
         */
        public void onReceive(Object message) {
            if (message instanceof Work) {
                Work work = (Work) message;
                double result = calculatePiFor(work.getiterations());
                getSender().tell(new Result(result), getSelf());
            } else {
                unhandled(message);
            }
        }
    }

    /**
     * The Class Master.
     */
    public static class Master extends UntypedAbstractActor {
        
        /** The iterations. */
        private final long iterations;
        
        /** The pi. */
        private double pi;
        
        /** The listener. */
        private final ActorRef listener;
        
        /** The worker router. */
        private final ActorRef workerRouter;

        /**
         * Instantiates a new master.
         *
         * @param nrOfWorkers the nr of workers
         * @param iterations the iterations
         * @param listener the listener
         */
        public Master(long nrOfWorkers, long iterations, ActorRef listener) {
            this.iterations = iterations;
            this.listener = listener;
            workerRouter = this.getContext().actorOf(new RoundRobinPool((int) nrOfWorkers).props(Props.create(Worker.class)), "workerRouter");
        }

        /* (non-Javadoc)
         * @see akka.actor.UntypedAbstractActor#onReceive(java.lang.Object)
         */
        public void onReceive(Object message) {
            if (message instanceof CalculatePI) {
            	workerRouter.tell(new Work(iterations), getSelf());
            } else if (message instanceof Result) {
                Result result = (Result) message;
                pi += result.getPI();
                listener.tell(new PiApproximation(pi), getSelf());
                getContext().stop(getSelf());
                
            } else {
                unhandled(message);
            }
        }
    }

    /**
     * The Class Listener.
     */
    public static class Listener extends UntypedAbstractActor {
        
        /* (non-Javadoc)
         * @see akka.actor.UntypedAbstractActor#onReceive(java.lang.Object)
         */
        public void onReceive(Object message) {
            if (message instanceof PiApproximation) {
                PiApproximation approximation = (PiApproximation) message;
                System.out.println(String.format("Pi approximation: %s", approximation.getPi()));
                getContext().system().terminate();
                latch.countDown();
            } else {
                unhandled(message);
            }
        }
    }
}
