package com.ipca.concorrente;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinPool;
import scala.concurrent.duration.Duration;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class PrimosTest {
		
	    @SuppressWarnings("serial")
	public static class CalculaIntervaloMessage implements Serializable {
        public final long num;
        public final ActorRef actorprimo;
        public CalculaIntervaloMessage(long num, ActorRef actorprimo) {
            this.num = num;
            this.actorprimo = actorprimo;
        }
    }
    @SuppressWarnings("serial")
	public static class CalculaPrimoMessage implements Serializable {
        public final long num;
        public CalculaPrimoMessage(long num) {
            this.num = num;
        }
    }
    @SuppressWarnings("serial")
	public static class RespostaPrimoMessage implements Serializable {
        public final boolean eprimo;
        public RespostaPrimoMessage(boolean eprimo) {
            this.eprimo = eprimo;
        }
    }
    @SuppressWarnings("serial")
	public static class QuantidadeIntervaloMessage implements Serializable {
    		public final long qnt;
        public QuantidadeIntervaloMessage(long qnt) {
    			this.qnt = qnt;
        }
    }
    
    public static class Controlador extends UntypedActor {
        long contaRespostas;
        long contaPrimos = 0;
        ActorRef actorPrimo;
        ActorRef actorIniciador;

        public void onReceive(Object message) {
        	
          if (message instanceof CalculaIntervaloMessage) {	
        	  
            this.contaRespostas= ((CalculaIntervaloMessage) message).num;
            this.actorPrimo = ((CalculaIntervaloMessage) message).actorprimo;
            this.actorIniciador = getSender();
            	
            for (long i=1; i<=contaRespostas; i++){
              actorPrimo.tell( new CalculaPrimoMessage(i), getSelf() );
            }
            	
            	System.out.println("Controlador: envio CalculaPrimoMessage " + contaRespostas);

            } else if (message instanceof RespostaPrimoMessage) {
            	
            	if ( ((RespostaPrimoMessage) message).eprimo )
            		contaPrimos += 1;
            	
            	contaRespostas -= 1;
            	
            	System.out.println("Controlador: ResultadoMessage");
            	
            	if (contaRespostas == 0)
            		actorIniciador.tell( 
            			new QuantidadeIntervaloMessage(contaPrimos), getSelf() );
            	
            } else unhandled(message);
        }
    }

    public static class Primo extends UntypedActor {
    	
        public void onReceive(Object message) {
        	
        	if (message instanceof CalculaPrimoMessage) {
        		
        		long num = ((CalculaPrimoMessage) message).num;
        		
//	        		try {
//						TimeUnit.SECONDS.sleep(num);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
        				
        		System.out.println("Primo: CalculaPrimoMessage " + num );
        		
        		getSender().tell(new RespostaPrimoMessage( isPrime(num) ), getSelf());

        	} else unhandled(message);
        }
    }
   
    public static boolean isPrime(long number)
	{ 
	  if (number < 2) return false; 
	  for (long i=2; i<number; i++) 
	    if (number % i == 0) return false; 
	  return true; 
	} 
    
    public static void main(String[] args) {
    	
        // Create the actor system
        final ActorSystem sistema = ActorSystem.create("primosakka");

        // Create the "actor-in-a-box"
        final Inbox inbox = Inbox.create(sistema);
        
        // Create the 'controlador' actor
        final ActorRef controlador = sistema.actorOf(Props.create(Controlador.class), "controlador");

        // Create the 'Primo' actor
//	        final ActorRef primo = sistema.actorOf(Props.create(Primo.class), "primo");

        // Create the Route actor; getContext() from actor
        // http://doc.akka.io/docs/akka/2.3.16/java/routing.html
        //
        final ActorRef primo = sistema.actorOf(new RoundRobinPool(4).props(Props.create(Primo.class)), 
        		    "router");
        
        // definir tamanho conjunto
        long N = 2000;
        
        // Ask the 'greeter for the latest 'greeting'
        // Reply should go to the "actor-in-a-box"
        inbox.send(controlador, new CalculaIntervaloMessage( N, primo));
        
        // Wait 5 seconds for the reply with the 'greeting' message
        QuantidadeIntervaloMessage resposta = (QuantidadeIntervaloMessage) 
        		inbox.receive(Duration.create(500, TimeUnit.SECONDS));
        
        long quantidade = resposta.qnt;
        
        System.out.println("Main: quantidade em "+ N +":"+ quantidade);
        
    	// shutdown
		sistema.shutdown();
	
    }

}
