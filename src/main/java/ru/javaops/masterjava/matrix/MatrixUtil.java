package ru.javaops.masterjava.matrix;

import java.util.*;
import java.util.concurrent.*;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final ExecutorCompletionService<Boolean> completionService = new ExecutorCompletionService<>(executor);
        final int matrixSize = matrixA.length;
         int[][] matrixC = new int[matrixSize][matrixSize];
        List<Future<Boolean>> futures = new LinkedList<>();

        for(int m = 0; m < matrixSize ; m = m + 100){
            int[] column = new int[matrixSize];
            int finalM = m;
            completionService.submit(() -> {
                for (int j = 0; j < matrixSize; j++) {
                    for (int k = 0; k < matrixSize; k++) {
                        column[k] = matrixB[k][j];
                    }

                    for (int i = finalM ; i < finalM + 100; i++) {
                        int[] row = matrixA[i];
                        int sum = 0;
                        for (int k = 0; k < matrixSize; k++) {
                            sum += row[k] * column[k];
                        }
                        matrixC[i][j] = sum;
                    }
                }
                        return true;
                    }
            ) ;

        }

        for (int i=0; i < 10; i++){
            final Future<Boolean> future = completionService.take();
        }
       return matrixC;

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
