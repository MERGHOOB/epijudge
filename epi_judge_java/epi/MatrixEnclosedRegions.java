package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class MatrixEnclosedRegions {

    public static void fillSurroundedRegions(List<List<Character>> board) {
        if (board.isEmpty()) {
            return;
        }

        List<List<Boolean>> visited = new ArrayList<>(board.size());
//        IntStream.range(0, board.size() - 1).forEach(i -> {
//            visited.add(new ArrayList<>(Collections.nCopies(board.get(i).size(), false)));
//        });

        for (List<Character> row : board) {
            visited.add(new ArrayList<>(Collections.nCopies(row.size(), false)));
        }

        // iterate on first and last column; we want to visit from boundary whites
        for (int i = 0; i < board.size(); i++) { // i represent row

            int firstColumn = 0;
            if (board.get(i).get(firstColumn) == 'W' && !visited.get(i).get(firstColumn)) {
                markBoundaryConnectedWhites(i, firstColumn, board, visited);
            }
            int lastColumn = board.get(i).size() - 1;
            if (board.get(i).get(lastColumn) == 'W' && !visited.get(i).get(lastColumn)) {
                markBoundaryConnectedWhites(i, lastColumn, board, visited);
            }
        }

        // iterate on first and last row; we want to visit whites from boundary whites
        for (int j = 0; j < board.get(0).size(); j++) {
            int firstRow = 0;
            if (board.get(firstRow).get(j) == 'W' && !visited.get(firstRow).get(j)) {
                markBoundaryConnectedWhites(firstRow, j, board, visited);
            }
            int lastRow = board.size() - 1;
            if (board.get(lastRow).get(j) == 'W' && !visited.get(lastRow).get(j)) {
                markBoundaryConnectedWhites(lastRow, j, board, visited);
            }
        }

        // replace whites which are not visited(enclosed whites) with blacks
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(i).size(); j++) {
                if (board.get(i).get(j) == 'W' && !visited.get(i).get(j)) {
                    board.get(i).set(j, 'B');
                }
            }
        }
    }

    private static void markBoundaryConnectedWhites(int x,
                                                    int y,
                                                    List<List<Character>> board,
                                                    List<List<Boolean>> visited) {
        int[][] DIRS = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        visited.get(x).set(y, true);
        for (int[] dir : DIRS) {
            int nextX = x + dir[0];
            int nextY = y + dir[1];
            if (nextX >= 0 && nextX < board.size()
                    && nextY >= 0 && nextY < board.get(nextX).size() && board.get(nextX).get(nextY) == 'W'
                    && !visited.get(nextX).get(nextY)) {
                markBoundaryConnectedWhites(nextX, nextY, board, visited);
            }
        }

    }

    @EpiTest(testDataFile = "matrix_enclosed_regions.tsv")
    public static List<List<Character>>
    fillSurroundedRegionsWrapper(List<List<Character>> board) {
        fillSurroundedRegions(board);
        return board;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "MatrixEnclosedRegions.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
