package com.ipca.distributed;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.routing.RoundRobinPool;
import com.typesafe.config.ConfigFactory;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Pi {

    static volatile CountDownLatch latch;
    static long timSum = 0;

    public static void main(String[] args) throws InterruptedException {
        Pi pi = new Pi();

        Scanner scannerObj = new Scanner(System.in);

        System.out.print("Informe a quantidade de itera��es a calcular: ");
        int numJobs = scannerObj.nextInt();
        System.out.println("N� Itera��es : " + numJobs);

        System.out.print("Informe a quantidade de atores: ");
        int MAX_ACT = scannerObj.nextInt();
        System.out.println("N� workers : " + MAX_ACT);

        System.out.print("Informe a quantidade de ciclos: ");
        int numStepsPerComp = scannerObj.nextInt();
        System.out.println("N� Mensagens : " + numStepsPerComp);

        //int numStepsPerComp = 1000;
        //int numJobs = 100000;
        //final int MAX_ACT = 16;
        String results[] = new String[MAX_ACT];

        for (int numActors = 1; numActors <= MAX_ACT; numActors++) {
            timSum = 0;
            for (int i = 0; i < 30; i++) {
                latch = new CountDownLatch(1);
                pi.calculate(numActors, numStepsPerComp, numJobs);
                latch.await();
                if ( i == 20 ) { // take last 10 samples only
                    timSum = 0;
                }
            }
            results[numActors-1] = "average "+numActors+" threads : "+(timSum/10/1000/1000);
        }

        for (int i = 0; i < results.length; i++) {
            String result = results[i];
            System.out.println(result);
        }
    }

    static class Calculate {
    }

    static class Work {
        private final int start;
        private final int nrOfElements;

        public Work(int start, int nrOfElements) {
            this.start = start;
            this.nrOfElements = nrOfElements;
        }

        public int getStart() {
            return start;
        }

        public int getNrOfElements() {
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
        private final long duration;

        public PiApproximation(double pi, long duration) {
            this.pi = pi;
            this.duration = duration;
        }

        public double getPi() {
            return pi;
        }

        public long getDuration() {
            return duration;
        }
    }

    public static class Worker extends UntypedAbstractActor {

        private double calculatePiFor(int start, int nrOfElements) {
            double acc = 0.0;
            for (int i = start * nrOfElements; i <= ((start + 1) * nrOfElements - 1); i++) {
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
        private final int nrOfMessages;
        private final int nrOfElements;

        private double pi;
        private int nrOfResults;
        private final long start = System.nanoTime();

        private final ActorRef listener;
        private final ActorRef workerRouter;

        public Master(
                final int nrOfWorkers,
                int nrOfMessages,
                int nrOfElements,
                ActorRef listener) {

            this.nrOfMessages = nrOfMessages;
            this.nrOfElements = nrOfElements;
            this.listener = listener;

            workerRouter = this.getContext().actorOf(new RoundRobinPool(nrOfWorkers).props(Props.create(Worker.class)), "workerRouter");

        }

        public void onReceive(Object message) {
            if (message instanceof Calculate) {
                for (int start = 0; start < nrOfMessages; start++) {
                    workerRouter.tell(new Work(start, nrOfElements), getSelf());
                }
            } else if (message instanceof Result) {
                Result result = (Result) message;
                pi += result.getValue();
                nrOfResults += 1;
                if (nrOfResults == nrOfMessages) {
                    // Send the result to the listener
                    long duration = System.nanoTime() - start;
                    listener.tell(new PiApproximation(pi, duration), getSelf());
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
                long duration = approximation.getDuration();
                System.out.println(String.format("Pi approximation: " +
                                "%s Calculation time: %s",
                        approximation.getPi(), (double)duration/1_000_000_000));
                timSum += duration;
                getContext().system().terminate();
                latch.countDown();
            } else {
                unhandled(message);
            }
        }
    }

    public void calculate(
            final int nrOfWorkers,
            final int nrOfElements,
            final int nrOfMessages) {


        // Create an Akka system
        ActorSystem system = ActorSystem.create("PiSystem");

        // create the result listener, which will print the result and shutdown the system
        //final ActorRef listener = system.actorOf(new Props(Listener.class), "listener");
        // create the result listener, which will print the result and shutdown the system
        //final ActorRef listener = system.actorOf(new Props(Listener.class), "listener");
        ActorRef listener = system.actorOf(Props.create(Listener.class), "listener");

        // create the master
        ActorRef master = system.actorOf(Props.create(Master.class, nrOfWorkers, nrOfMessages, nrOfElements, listener), "master");


        // start the calculation
        master.tell(new Calculate(), master);
    }
}