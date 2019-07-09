package com.ipca.distributed;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * The Class SequencialGregLei.
 */
public class Implementations {

	/**
	 * The main method.
	 *
	 * @param args            the arguments
	 * @throws InterruptedException the interrupted exception
	 * @throws ExecutionException the execution exception
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		Scanner input = new Scanner(System.in);
		
		while (true) {
			
			System.out.println("Which implementation do you want to run?");
			System.out.println("1) Gregory-Leibniz Distributed");
			System.out.println("2) Monte-Carlo Distributed");
			System.out.println("================");
			System.out.print("Select an option: ");
						
			if (input.hasNextInt()) {
				int menu = input.nextInt();

				switch (menu) {
				case 1:
					System.out.print("How many iterations?? ");

					long iterations = 0l;
					if (input.hasNextLong()) {
						iterations = input.nextLong();
						
						System.out.print("How many distributed calculations?? ");
						
						if(input.hasNextInt()) {
							
							int concurrentNr = input.nextInt();
							
							long startTime = System.nanoTime();
							GregoryLeibniz gregoryLeibniz = new GregoryLeibniz();
							gregoryLeibniz.gregoryLeibnizDistribuitedMethod(iterations, concurrentNr);
							long endTime = System.nanoTime();

							
							System.out.println("Executed in " + ((endTime - startTime) / 1e6) + " ms.");
							System.out.println("===========================\n");					
						}
						else {
							System.out.println("Invalid number!!!\n");	
						}

						
					} else {
						System.out.println("Invalid number!!!\n");	
					}
				break;
				case 2:
					System.out.print("How many iterations?? ");

					iterations = 0l;
					if (input.hasNextLong()) {
						iterations = input.nextLong();
						
						System.out.print("How many distributed calculations?? ");
						
						if(input.hasNextInt()) {
							
							int concurrentNr = input.nextInt();
							
							long startTime = System.nanoTime();
							MonteCarlo monteCarlo = new MonteCarlo();
							monteCarlo.monteCarloDistribuitedMethod(iterations, concurrentNr);
							long endTime = System.nanoTime();

							
							System.out.println("Executed in " + ((endTime - startTime) / 1e6) + " ms.");
							System.out.println("===========================\n");					
						}
						else {
							System.out.println("Invalid number!!!\n");	
						}

						
					} else {
						System.out.println("Invalid number!!!\n");	
					}
				break;
				default:
					System.out.println("Invalid number!!!\n");	
					break;
				}
			} else {
				System.out.println("Invalid number!!!\n");				
				if(input.hasNext()) {
					input.nextLine();
				}
			}
		}
	}
}
