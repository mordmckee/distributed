package com.ipca;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * The Class SequencialGregLei.
 */
public class Implementations {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		Scanner input = new Scanner(System.in);
		
		while (true) {
			
		
			System.out.println("Which implementation do you want to run?");
			System.out.println("1) Monte-Carlo Sequencial");
			System.out.println("2) Gregory-Leibniz Sequencial");
			System.out.println("3) Monte-Carlo Concurrent");
			System.out.println("4) Gregory-Leibniz Concurrent");
			System.out.println("================");
			System.out.print("Select an option: ");
						
			if (input.hasNextInt()) {
				int menu = input.nextInt();

				switch (menu) {
				/*
				 * Desenvolva uma aplicação sequencial (i.e., sem concorrência)
				 * para o cálculo de PI usando o método de Monte-Carlo que
				 * recebe como entrada a quantidade de pontos a gerar e produz
				 * como resultado o valor estimado de PI.
				 */
				case 1:
					System.out.print("How many points? ");

					long points = 0l;
					if (input.hasNextLong()) {
						points = input.nextLong();

						long startTime = System.nanoTime();
						double estimatedPI = MonteCarlo.monteCarloMethodSequencialMethod(points);
						long endTime = System.nanoTime();

						System.out.println("Sequencial Monte-Carlo estimated PI value : " + estimatedPI
								+ ". Executed in " + ((endTime - startTime) / 1e6) + " ms.");
						System.out.println("===========================\n");
					} else {
						System.out.println("Invalid number!!!\n");	
					}
				break;
				case 2:
					System.out.print("How many points? ");

					if (input.hasNextLong()) {
						points = input.nextLong();

						long startTime = System.nanoTime();
						double estimatedPI = GregoryLeibniz.gregoryLeibnizSequencialMethod(points);
						long endTime = System.nanoTime();

						System.out.println("Sequencial Gregory-Leibniz estimated PI value : " + estimatedPI
								+ ". Executed in " + ((endTime - startTime) / 1e6) + " ms.");
						System.out.println("===========================\n");
					} else {
						System.out.println("Invalid number!!!\n");	
					}
				break;
				case 3:
					System.out.print("How many points? ");

					points = 0l;
					if (input.hasNextLong()) {
						points = input.nextLong();
					} else {
						System.out.println("Invalid number!!!\n");	
					}
					System.out.print("How many threads? ");
					int nThreads = 0;
					if (input.hasNextInt()) {
						nThreads = input.nextInt();

						long startTime = System.nanoTime();
						double estimatedPI = MonteCarlo.monteCarloMethodConcorrentMethod(points, nThreads);

						long endTime = System.nanoTime();

						System.out.println("Concurrent Monte-Carlo estimated PI value : " + estimatedPI
								+ ". Executed in " + ((endTime - startTime) / 1e6) + " ms.");
						System.out.println("===========================\n");
					} else {
						System.out.println("Invalid number!!!\n");	
					}
				break;
				case 4:
					System.out.print("How many points? ");

					if (input.hasNextLong()) {
						points = input.nextLong();

						System.out.print("How many threads? ");

						nThreads = 0;
						if (input.hasNextInt()) {
							nThreads = input.nextInt();

							long startTime = System.nanoTime();
							double estimatedPI = GregoryLeibniz.gregoryLeibnizConcorrentMethod(points, nThreads);

							long endTime = System.nanoTime();

							System.out.println("Concurrent Gregory-Leibniz estimated PI value : " + estimatedPI
									+ ". Executed in " + ((endTime - startTime) / 1e6) + " ms.");
							System.out.println("===========================\n");
						} else {
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
