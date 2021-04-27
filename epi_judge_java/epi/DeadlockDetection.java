package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.EpiUserType;
import epi.test_framework.GenericTest;
import epi.test_framework.TimedExecutor;

import java.util.ArrayList;
import java.util.List;

public class DeadlockDetection {

    public static class GraphVertex {
        public List<GraphVertex> edges;

        public GraphVertex() {
            edges = new ArrayList<>();
        }

        public Color color;

        private enum Color {WHITE, GRAY, BLACK}
    }

    public static boolean isDeadlocked(List<GraphVertex> graph) {

        graph.forEach(graphVertex -> {
            graphVertex.color = GraphVertex.Color.WHITE;
        });

        for (GraphVertex graphVertex : graph) {
            if (hasCycle(graphVertex)) {
                return true;
            }
        }

        return false;
    }

    private static boolean hasCycle(GraphVertex curr) {
        if (curr.color == GraphVertex.Color.GRAY) {
            return true;
        }

        curr.color = GraphVertex.Color.GRAY;
        for (GraphVertex next : curr.edges) {
            if (next.color != GraphVertex.Color.BLACK) {
                if (hasCycle(next)) {
                    return true;
                }
            }
        }
        curr.color = GraphVertex.Color.BLACK;
        return false;
    }

    @EpiUserType(ctorParams = {int.class, int.class})
    public static class Edge {
        public int from;
        public int to;

        public Edge(int from, int to) {
            this.from = from;
            this.to = to;
        }
    }

    @EpiTest(testDataFile = "deadlock_detection.tsv")
    public static boolean isDeadlockedWrapper(TimedExecutor executor,
                                              int numNodes, List<Edge> edges)
            throws Exception {
        if (numNodes <= 0) {
            throw new RuntimeException("Invalid numNodes value");
        }
        List<GraphVertex> graph = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) {
            graph.add(new GraphVertex());
        }
        for (Edge e : edges) {
            if (e.from < 0 || e.from >= numNodes || e.to < 0 || e.to >= numNodes) {
                throw new RuntimeException("Invalid vertex index");
            }
            graph.get(e.from).edges.add(graph.get(e.to));
        }

        return executor.run(() -> isDeadlocked(graph));
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "DeadlockDetection.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
