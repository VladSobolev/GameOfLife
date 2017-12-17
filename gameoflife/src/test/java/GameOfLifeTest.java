import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by Mintas on 12/10/2017.
 */
public class GameOfLifeTest {
    private GameOfLife gameOfLife;

    @Before
    public void before() {
        gameOfLife = new GameOfLifeImpl();
    }

    @Test
    public void testGame1000() throws Exception {
        testOneGame("resources/input1000.txt", "resources/output1000.txt");
    }

    @Test
    public void testGame10000() throws Exception {
        testOneGame("resources/input10000.txt", "resources/output10000.txt");
    }

    @Test
    public void testGame10() throws Exception {
        testOneGame("resources/input.txt", "resources/output.txt");
    }

    @Test
    public void testGame100() throws Exception {
        testOneGame("resources/input100.txt", "resources/output100.txt");
    }

    private void testOneGame(String inputFile, String expectedOutputFile) throws FileNotFoundException {
        int[][] result = gameOfLife.play(inputFile);
        assertArrayEquals(readFile(expectedOutputFile, result.length), result);
    }

    private static int[][] readFile(String fileName, int n) throws FileNotFoundException {
        int[][] nowState = new int[n][n];
        Scanner scanner = new Scanner(new FileInputStream(fileName));
        for (int i = 0; i < n; ++i) {
            String s = scanner.nextLine();
            for (int j = 0; j < n; ++j)
                nowState[i][j] = Character.getNumericValue(s.charAt(j));
        }
        return nowState;
    }
}
