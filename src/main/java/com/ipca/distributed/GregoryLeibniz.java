package com.ipca.distributed;

import java.util.concurrent.CountDownLatch;
import com.ipca.distributed.Pi.Calculate;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.routing.RoundRobinPool;

/**
 * The Class GregoryLeibniz.
 */
public class GregoryLeibniz {
		
	static volatile CountDownLatch latch;
		
	/**
	 * Gregory leibniz distribuited method.
	 *
	 * @param iterations the iterations
	 * @param concurrentNr the concurrent nr
	 * @return the double
	 * @throws InterruptedException 
	 */
	public void gregoryLeibnizDistribuitedMethod(long iterations, int concurrentNr) throws InterruptedException {
		
		GregoryLeibniz gregoryLeibniz = new GregoryLeibniz();
		
        for (int numActors = 1; numActors <= concurrentNr; numActors++) {
            //latch = new CountDownLatch(1);
            gregoryLeibniz.actorPI(numActors, iterations, concurrentNr);
            //latch.await();
        }
	}
	
	public void actorPI(final long nrOfWorkers, final long nrOfElements, final long nrOfMessages) {
		  
        ActorSystem system = ActorSystem.create("PiSystem");

        ActorRef listener = system.actorOf(Props.create(Listener.class), "listener");

        ActorRef master = system.actorOf(Props.create(Master.class, nrOfWorkers, nrOfMessages, nrOfElements, listener), "master");

        master.tell(new Calculate(), master);
	  }
	
	static class CalculatePI { }
	
	static class Work {
        private final long start;
        private final long nrOfElements;

        public Work(long start, long nrOfElements) {
            this.start = start;
            this.nrOfElements = nrOfElements;
        }

        public long getStart() {
            return start;
        }

        public long getNrOfElements() {
            return nrOfElements;
        }
    }

    static class Result {
        private final double value;

        public Result(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }
    }

    static class PiApproximation {
        private final double pi;

        public PiApproximation(double pi) {
            this.pi = pi;
        }

        public double getPi() {
            return pi;
        }
    }

    public static class Worker extends UntypedAbstractActor {

        private double calculatePiFor(long start, long nrOfElements) {
            double acc = 0.0;
            for (long i = start * nrOfElements; i <= ((start + 1) * nrOfElements - 1); i++) {
                acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1);
            }
            return acc;
        }

        public void onReceive(Object message) {
            if (message instanceof Work) {
                Work work = (Work) message;
                double result = calculatePiFor(work.getStart(), work.getNrOfElements());
                getSender().tell(new Result(result), getSelf());
            } else {
                unhandled(message);
            }
        }
    }

    public static class Master extends UntypedAbstractActor {
        private final long nrOfMessages;
        private final long nrOfElements;

        private double pi;
        private long nrOfResults;
        private final ActorRef listener;
        private final ActorRef workerRouter;

        public Master(
                final long nrOfWorkers,
                long nrOfMessages,
                long nrOfElements,
                ActorRef listener) {

            this.nrOfMessages = nrOfMessages;
            this.nrOfElements = nrOfElements;
            this.listener = listener;

            workerRouter = this.getContext().actorOf(new RoundRobinPool((int) nrOfWorkers).props(Props.create(Worker.class)), "workerRouter");

        }

        public void onReceive(Object message) {
            if (message instanceof Calculate) {
                for (long start = 0; start < nrOfMessages; start++) {
                    workerRouter.tell(new Work(start, nrOfElements), getSelf());
                }
            } else if (message instanceof Result) {
                Result result = (Result) message;
                pi += result.getValue();
                nrOfResults += 1;
                if (nrOfResults == nrOfMessages) {
                    listener.tell(new PiApproximation(pi), getSelf());
                    // Stops this actor and all its supervised children
                    getContext().stop(getSelf());
                }
            } else {
                unhandled(message);
            }
        }
    }

    public static class Listener extends UntypedAbstractActor {
        public void onReceive(Object message) {
            if (message instanceof PiApproximation) {
                PiApproximation approximation = (PiApproximation) message;
                System.out.println(String.format("Pi approximation: %s", approximation.getPi()));
                getContext().system().terminate();
                //latch.countDown();
            } else {
                unhandled(message);
            }
        }
    }
	
}



