package com.ipca;

import java.util.Scanner;

public class SequencialGregLei {


    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        System.out.println("Qual o número de pontos que pretende gerar?");
        long amount = input.nextInt();

        long startTime = System.nanoTime();

        double piCalculated = gregLei(amount);             // <----------------------- Chama o método

        long endTime = System.nanoTime();


        System.out.println("Valor estimado de \u03C0: " + piCalculated);

        double difference = (endTime - startTime) / 1e6;


        System.out.print("O programa demorou " + difference + " milisegundos a ser executado!");

    }

    public static double gregLei(int numberTotalGenerated) {

        double factor = 1.0;
        double sum = 0.0;


        for (int k = 1; k < numberTotalGenerated; k =k+2) {

            sum += factor/k;
            factor = -factor;

            }
        return 4 * sum;
    }
}


