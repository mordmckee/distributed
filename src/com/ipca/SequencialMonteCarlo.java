package com.ipca;

import java.util.Scanner;

public class SequencialMonteCarlo {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        System.out.println("Qual o número de pontos que pretende gerar?");
        int amount = input.nextInt();

        long startTime = System.nanoTime();

        double piCalculated = monteCarlo(amount);             // <----------------------- Chama o método

        long endTime = System.nanoTime();

        System.out.println("Valor estimado de \u03C0: " + piCalculated);

        double difference = (endTime - startTime) / 1e6;

        System.out.print("O programa demorou " + difference + " milisegundos a ser executado!");

    }

    public static double monteCarlo(int numberTotalGenerated) {

        int inCircle = 0;

        for (int i = 1; i <= numberTotalGenerated; i++) {

            double randomX = (Math.random() * 2) - 1;                    //entre -1 e 1
            double randomY = (Math.random() * 2) - 1;                    //entre -1 e 1

            //Formula distância entre os pontos
            double dist = Math.sqrt(randomX * randomX + randomY * randomY);

            if (dist < 1) {                                             //circle with diameter of 2 has radius of 1
                inCircle++;
            }
        }

        return 4.0 * inCircle / numberTotalGenerated;

    }
}