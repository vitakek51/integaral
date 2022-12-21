/*
Задача 1 ✫
Написать программу, вычисляющую в n = 1..10 потоков интеграл функции f(x)
на заданном участке [a,b], с заданной шагом h методом трапеций.
Значения: f(x), a, b, h - заданы для каждого варианта.
Программа должна выводить статистику по времени затраченному для вычисления с заданым количеством потоков.
Пример:
4 - 2375.4 мс.
5 - 2385.4 мс.
6 - 2432.4 мс.
...
10 - 4896.8 мс.
Вариант 1
f(x) = sin(x), [0,π], h = 10E-6
 */

public class Main extends Thread {
    private final double intervalStart = 0;
    private final double intervalEnd = Math.PI;
    private final double step = 0.0000001;

    private int totalIterations = 0;
    private Thread[] threads;
    private double funcValue;

    private final static Object lock = new Object();

    //функция - sin(x)
    //Производная - -cos(x)

    public Main() {
        for (double i = intervalStart; i < intervalEnd; i += step) {
            totalIterations++;
        }

        TestThread(1);
        TestThread(2);
        TestThread(3);
        TestThread(4);
        TestThread(5);
        TestThread(6);
        TestThread(7);
        TestThread(8);
        TestThread(9);
        TestThread(10);
    }

    public static void main(String[] args) {
        new Thread(new Main()).start();
    }

    private void TestThread(int numberOfThreads) {
        funcValue = 0;
        threads = new Thread[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            int startPoint = totalIterations / numberOfThreads * i;
            int endPoint = totalIterations / numberOfThreads * (i + 1);
            threads[i] = new Thread(solveIntegral(startPoint, endPoint));
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        long startTime = System.currentTimeMillis();

        while (true) {
            boolean passed = false;
            for (Thread thread : threads) {
                if (thread.isAlive()) {
                    passed = true;
                    break;
                }
            }
            if (!passed) {
                break;
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Total time: " + (endTime - startTime) / 1_000D + " seconds with " + numberOfThreads + " threads");
        //System.out.println("function value: " + funcValue);
    }

    private Runnable solveIntegral(int startPoint, int endPoint) {
        return () -> {
            double value = 0;
            for (int j = startPoint; j < endPoint; j++) {
                value += -1 * Math.cos(j * step) + Math.cos(j * step - step);
            }
            synchronized (lock) {
                funcValue += value;
            }
        };
    }
}
