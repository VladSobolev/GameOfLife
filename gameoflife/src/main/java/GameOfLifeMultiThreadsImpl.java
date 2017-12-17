import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class GameOfLifeMultiThreadsImpl implements GameOfLife {
    private int nTh;

    public GameOfLifeMultiThreadsImpl(int nTh) {
        this.nTh = nTh;
    }

    @Override
    public int[][] play(String inputFile) {
        try (Scanner scanner = new Scanner(new FileInputStream(inputFile))) {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            int[][] nowState = new int[n + 2][n + 2];
            scanner.nextLine();
            for (int i = 0; i < n; ++i) {
                String s = scanner.nextLine();
                for (int j = 0; j < n; ++j)
                    nowState[i + 1][j + 1] = Character.getNumericValue(s.charAt(j));
            }
            for (int i = 0; i < n; ++i) {
                nowState[0][i + 1] = nowState[n][i + 1];
                nowState[n + 1][i + 1] = nowState[1][i + 1];
            }
            for (int i = 0; i < n + 2; ++i) {
                nowState[i][0] = nowState[i][n];
                nowState[i][n + 1] = nowState[i][1];
            }
            for (int i = 0; i < m; ++i) {
                int[][] futureState = new int[n + 2][n + 2];
                List<Thread> threads = new ArrayList<>();
                int nRowsToThread = (n + nTh - 1) / nTh;
                for (int thI = 0; thI < nTh; ++thI) {
                    int firstRow = thI * nRowsToThread;
                    int lastRow = Math.min(n, firstRow + nRowsToThread);
                    threads.add(new Thread(new Task(firstRow, lastRow, n, nowState, futureState)));
                }
                for (int thI = 0; thI < nTh; ++thI) {
                    threads.get(thI).start();
                }
                for (int thI = 0; thI < nTh; ++thI) {
                    threads.get(thI).join();
                }

                for (int j = 0; j < n; ++j) {
                    futureState[0][j + 1] = futureState[n][j + 1];
                    futureState[n + 1][j + 1] = futureState[1][j + 1];
                }
                for (int j = 0; j < n + 2; ++j) {
                    futureState[j][0] = futureState[j][n];
                    futureState[j][n + 1] = futureState[j][1];
                }
                nowState = futureState;
            }
            int[][] finalState = new int[n][n];
            for (int i = 0; i < n; ++i)
                for (int j = 0; j < n; ++j) {
                    finalState[i][j] = nowState[i + 1][j + 1];
                }
            return finalState;
        } catch (FileNotFoundException | InterruptedException e) {
            return null;
        }
    }

    public static class Task implements Runnable {
        private int firstRow;
        private int lastRow;
        private int n;
        private int[][] nowState;
        private int[][] futureState;

        public Task(int firstRow, int lastRow, int n, int[][] nowState, int[][] futureState) {
            this.firstRow = firstRow;
            this.lastRow = lastRow;
            this.n = n;
            this.nowState = nowState;
            this.futureState = futureState;
        }

        @Override
        public void run() {
            for (int j = firstRow; j < lastRow; ++j)
                for (int k = 0; k < n; ++k) {
                    int numberOfFriends = nowState[j][k] + nowState[j][k + 1] + nowState[j][k + 2] +
                            nowState[j + 1][k] + nowState[j + 1][k + 2] + nowState[j + 2][k] +
                            nowState[j + 2][k + 1] + nowState[j + 2][k + 2];
                    if (numberOfFriends == 3 || (numberOfFriends == 2 && nowState[j + 1][k + 1] == 1)) {
                        futureState[j + 1][k + 1] = 1;
                    } else {
                        futureState[j + 1][k + 1] = 0;
                    }
                }
        }
    }
}
