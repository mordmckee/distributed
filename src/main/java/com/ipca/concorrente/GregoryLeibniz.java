package com.ipca.concorrente;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinPool;
import scala.concurrent.duration.Duration;

/**
 * The Class GregoryLeibniz.
 */
public class GregoryLeibniz {
	
	/**
	 * Gregory leibniz distribuited method.
	 *
	 * @param iterations the iterations
	 * @param concurrentNr the concurrent nr
	 * @return the double
	 */
	public double gregoryLeibnizDistribuitedMethod(long iterations, int concurrentNr) {
		
		final ActorSystem system = ActorSystem.create("gregoryLeibnizAKKA");
		
		final Inbox inbox = Inbox.create(system);
		
		final ActorRef controller = system.actorOf(Props.create(Controller.class), "controller");
		
		final ActorRef piActorRef = system.actorOf(new RoundRobinPool(concurrentNr).props(Props.create(PI.class)), "router");
		
		inbox.send(controller, new CalculateIntervalMessage(iterations, piActorRef));
		
		ResponsePIMessage response = (ResponsePIMessage) inbox.receive(Duration.create(500, TimeUnit.SECONDS));
		
		double pi = response.getPi();
					
		system.shutdown();
		
		return pi;
		
	}
	
	/**
	 * The Class Controller.
	 */
	public static class Controller extends UntypedActor {
		
		/** The count responses. */
		long countResponses;
		
		/** The pi. */
		double pi = 0;
		
		/** The actor PI. */
		ActorRef actorPI;
		
		/** The actor iniciator. */
		ActorRef actorIniciator;
		
		/**
		 * On receive.
		 *
		 * @param message the message
		 */
		public void onReceive(Object message) {
			if(message instanceof CalculateIntervalMessage) {
				this.countResponses = ((CalculateIntervalMessage) message).getNum();
				this.actorPI = ((CalculateIntervalMessage) message).getActorPI();
				this.actorIniciator = getSender();
				
				for(long i = 0; i < countResponses; i++) {
					actorPI.tell(new CalculatePIMessage(i), getSelf());
				}
				
				//System.out.println("Controller: Send CalculatePIMessage -> " + countResponses);
			}
			else if(message instanceof ResponsePIMessage) {
				pi = ((ResponsePIMessage) message).getPi();
				
				countResponses -= 1;
				
				//System.out.println("Controller: ResultMessage");
				
				if(countResponses == 0) {
					actorIniciator.tell(new ResponsePIMessage(pi), getSelf());
				}
			}
		}
	}
	
	/**
	 * Calculate PI.
	 *
	 * @param n the n
	 * @return the double
	 */
	public static double calculatePI(long n) {
		double factor = 1.0;
		double sum = 0.0;

		for (long k = 0; k < n; k++) {
			if(k % 2 == 0) factor = 1.0;
			else factor = -1;
			
			sum += factor / (2 * k + 1);
		}
		return 4.0 * sum;
	}
	
	/**
	 * The Class PI.
	 */
	public static class PI extends UntypedActor {
		
		/**
		 * On receive.
		 *
		 * @param message the message
		 */
		public void onReceive(Object message) {
			if(message instanceof CalculatePIMessage) {
				long num = ((CalculatePIMessage) message).getNum();
				
				//System.out.println("PI: CalculatePIMessage -> " + num);
				
				getSender().tell(new ResponsePIMessage(calculatePI(num)), getSelf());
			}
			else {
				unhandled(message);
			}
		}
	}
	
	/**
	 * The Class CalculatePIMessage.
	 */
	public static class CalculatePIMessage implements Serializable {
		
		/** The num. */
		private long num;
		
		/**
		 * Instantiates a new calculate PI message.
		 *
		 * @param num the num
		 */
		public CalculatePIMessage(long num) {
			this.num = num;
		}

		/**
		 * Gets the num.
		 *
		 * @return the num
		 */
		public long getNum() {
			return num;
		}

		/**
		 * Sets the num.
		 *
		 * @param num the new num
		 */
		public void setNum(long num) {
			this.num = num;
		}
	}
	
	/**
	 * The Class ResponsePIMessage.
	 */
	public static class ResponsePIMessage {
		
		/** The pi. */
		private double pi;
		
		/**
		 * Instantiates a new response PI message.
		 *
		 * @param pi the pi
		 */
		public ResponsePIMessage(double pi) {
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
		
		/**
		 * Sets the pi.
		 *
		 * @param pi the new pi
		 */
		public void setPi(double pi) {
			this.pi = pi;
		}
	}
	
	/**
	 * The Class CalculateIntervalMessage.
	 */
	public static class CalculateIntervalMessage {
	   
   	/** The num. */
   	private long num;
        
        /** The actor PI. */
        public ActorRef actorPI;
        
        /**
         * Instantiates a new calculate interval message.
         *
         * @param num the num
         * @param actorPI the actor PI
         */
        public CalculateIntervalMessage(long num, ActorRef actorPI) {
            this.num = num;
            this.actorPI = actorPI;
        }
		
		/**
		 * Gets the num.
		 *
		 * @return the num
		 */
		public long getNum() {
			return num;
		}
		
		/**
		 * Sets the num.
		 *
		 * @param num the new num
		 */
		public void setNum(long num) {
			this.num = num;
		}
		
		/**
		 * Gets the actor PI.
		 *
		 * @return the actor PI
		 */
		public ActorRef getActorPI() {
			return actorPI;
		}
		
		/**
		 * Sets the actor PI.
		 *
		 * @param actorPI the new actor PI
		 */
		public void setActorPI(ActorRef actorPI) {
			this.actorPI = actorPI;
		}
	}
}



