package ru.javaops.masterjava.matrix;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
         int[][] matrixC = new int[matrixSize][matrixSize];
         int index = 0;

         Future<Boolean> future1 = executor.submit(() -> {
             for (int i = 0; i < matrixSize/2; i++) {
                 for (int j = 0; j < matrixB[0].length; j++) {
                     for (int k = 0; k < matrixA[0].length; k++) {
                         matrixC[i][j] += matrixA[i][k] * matrixB[k][j];
                     }
                 }
             }
             return true;
         }
         );
        Future<Boolean> future2 = executor.submit(() -> {

                    for (int i = matrixSize/2; i < matrixSize; i++) {
                        for (int j = 0; j < matrixB[0].length; j++) {
                            for (int k = 0; k < matrixA[0].length; k++) {
                                matrixC[i][j] += matrixA[i][k] * matrixB[k][j];
                            }
                        }
                    }
                    return true;
                }
        );
        while (true){
            if (future1.isDone() && future2.isDone())
                break;
        }

      //  Thread.sleep(100);

//        System.out.println("multi " + matrixC[0][0] + " last element " + matrixC[0][matrixSize-1]);
//        System.out.println("multi " + matrixC[matrixSize-1][0] + " last element " + matrixC[matrixSize-1][matrixSize-1]);
            return matrixC;

    }
    public static void multiThreadMultiply(int[][] matrixA, int[][] matrixB, int[][] matrixC, int index) {

        for (int j = 0; j < matrixB[0].length; j++) {
            for (int k = 0; k < matrixA[0].length; k++) {
                matrixC[index][j] += matrixA[index][k] * matrixB[k][j];
            }
        }
    }


    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        int[] column = new int[matrixSize];

        for (int j = 0; j < matrixSize; j++) {
            for (int k = 0; k < matrixSize; k++) {
                column[k] = matrixB[k][j];
            }

            for (int i = 0; i < matrixSize; i++) {
                int[] row = matrixA[i];
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += row[k] * column[k];
                }
                matrixC[i][j] = sum;
            }
        }
//        System.out.println("single " + matrixC[0][0] + " last element " + matrixC[0][matrixSize-1]);
//        System.out.println("single " + matrixC[matrixSize-1][0] + " last element " + matrixC[matrixSize-1][matrixSize-1]);
        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
