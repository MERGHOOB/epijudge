package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TimedExecutor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MatrixConnectedRegions {
    public static void flipColor(int x, int y, List<List<Boolean>> image) {

        final int[][] DIRS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        boolean color = image.get(x).get(y);

        Queue<Coordinate> queue = new LinkedList<>();
        image.get(x).set(y, !color);
        queue.add(new Coordinate(x, y));
        while (!queue.isEmpty()) {
            Coordinate curr = queue.element();
            for (int[] dir : DIRS) {
                Coordinate next = new Coordinate(curr.x + dir[0], curr.y + dir[1]);
                if (isFeasible(next, image, color)) {
                    image.get(next.x).set(next.y, !color);
                    queue.add(next);
                }
            }

            queue.remove();
        }
    }

    private static boolean isFeasible(Coordinate next, List<List<Boolean>> image, boolean color) {
        return next.x >= 0 && next.x < image.size() && next.y >= 0 && next.y < image.get(next.x).size()
                && image.get(next.x).get(next.y) == color;
    }

    @EpiTest(testDataFile = "painting.tsv")
    public static List<List<Integer>> flipColorWrapper(TimedExecutor executor,
                                                       int x, int y,
                                                       List<List<Integer>> image)
            throws Exception {
        List<List<Boolean>> B = new ArrayList<>();
        for (int i = 0; i < image.size(); i++) {
            B.add(new ArrayList<>());
            for (int j = 0; j < image.get(i).size(); j++) {
                B.get(i).add(image.get(i).get(j) == 1);
            }
        }

        executor.run(() -> flipColor(x, y, B));

        image = new ArrayList<>();
        for (int i = 0; i < B.size(); i++) {
            image.add(new ArrayList<>());
            for (int j = 0; j < B.get(i).size(); j++) {
                image.get(i).add(B.get(i).get(j) ? 1 : 0);
            }
        }

        return image;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "MatrixConnectedRegions.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    private static class Coordinate {
        int x, y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
