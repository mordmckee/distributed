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

			int menu = input.nextInt();

			switch (menu) {
			/*
			 * Desenvolva uma aplicação sequencial (i.e., sem concorrência) para
			 * o cálculo de PI usando o método de Monte-Carlo que recebe como
			 * entrada a quantidade de pontos a gerar e produz como resultado o
			 * valor estimado de PI.
			 */
			case 1:
				System.out.print("How many points? ");
				long points = input.nextLong();
				long startTime = System.nanoTime();
				double estimatedPI = MonteCarlo.monteCarloMethodSequencialMethod(points);
				long endTime = System.nanoTime();

				System.out.println("Sequencial Monte-Carlo estimated PI value : " + estimatedPI + ". Executed in "
						+ ((endTime - startTime) / 1e6) + " ms.");
				System.out.println("===========================\n");

				break;
			/*
			 * Desenvolva uma aplicação sequencial (i.e., sem concorrência) para
			 * o cálculo de PI usando o método de Séries de Gregory-Leibniz que
			 * recebe como entrada a quantidade de iterações k a calcular e
			 * produz como resultado o valor estimado de PI.
			 */
			case 2:
				System.out.print("How many points? ");
				points = input.nextLong();
				startTime = System.nanoTime();
				estimatedPI = GregoryLeibniz.gregoryLeibnizSequencialMethod(points);
				endTime = System.nanoTime();

				System.out.println("Sequencial Gregory-Leibniz estimated PI value : " + estimatedPI + ". Executed in "
						+ ((endTime - startTime) / 1e6) + " ms.");
				System.out.println("===========================\n");

				break;
			/*
			 * Apresente uma versão concorrente da aplicação com o método de
			 * Monte-Carlo, sendo que o programa deverá receber como entrada e
			 * produzir como resultado a mesma informação do programa
			 * sequencial. Adicionalmente, esta versão concorrente deve permitir
			 * também especificar como parâmetro de entrada o número de threads
			 */
			case 3:
				System.out.print("How many points? ");
				points = input.nextLong();
				System.out.print("How many threads? ");
				int nThreads = input.nextInt();
				
				startTime = System.nanoTime();
				MonteCarlo monteCarloObj = new MonteCarlo(points, nThreads);
				estimatedPI = monteCarloObj.calculatePI();
			
				endTime = System.nanoTime();

				System.out.println("Concurrent Monte-Carlo estimated PI value : " + estimatedPI + ". Executed in "
						+ ((endTime - startTime) / 1e6) + " ms.");
				System.out.println("===========================\n");

				break;
			case 4:
				System.out.print("How many points? ");
				points = input.nextLong();
				System.out.print("How many threads? ");
				nThreads = input.nextInt();
				
				startTime = System.nanoTime();
				GregoryLeibniz gregoryLeibnizObj = new GregoryLeibniz(points, nThreads);
				estimatedPI = gregoryLeibnizObj.calculatePI();
			
				endTime = System.nanoTime();

				System.out.println("Concurrent Gregory-Leibniz estimated PI value : " + estimatedPI + ". Executed in "
						+ ((endTime - startTime) / 1e6) + " ms.");
				System.out.println("===========================\n");

				break;
			}
		}
	}
}
