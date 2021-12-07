package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        HashMap<V, E> spt = new HashMap<>();
        if (Objects.equals(start, end)) {
            return spt;
        }

        if (graph.outgoingEdgesFrom(start).size() == 0) {
            return spt;
        }

        Set<V> known = new HashSet<>();
        HashMap<V, Double> distTo = new HashMap<>();
        ExtrinsicMinPQ<E> heap = createMinPQ();

        known.add(start);
        distTo.put(start, 0.0);
        for (E edge : graph.outgoingEdgesFrom(start)) {
            heap.add(edge, edge.weight());
        }

        while (heap.size() > 0 && !known.contains(end)) {
            E e = heap.removeMin();
            known.add(e.to());

            double distToU = distTo.get(e.from());
            double prevDistToV = Double.POSITIVE_INFINITY;
            if (distTo.containsKey(e.to())) {
                prevDistToV = distTo.get(e.to());
            }
            if (distToU + e.weight() < prevDistToV) {
                distTo.put(e.to(), distToU + e.weight());
                spt.put(e.to(), e);
            }

            for (E edge : graph.outgoingEdgesFrom(e.to())) {
                if (!known.contains(edge.to()) && !heap.contains(edge)) {
                    heap.add(edge, distTo.get(edge.from()) + edge.weight());
                }
            }
        }

        return spt;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }

        E edge = spt.get(end);
        if (edge == null) {
            return new ShortestPath.Failure<>();
        }

        List<E> path = new LinkedList<>();
        path.add(edge);
        V u = edge.from();
        while (!u.equals(start)) {
            edge = spt.get(u);
            path.add(0, edge);
            u = edge.from();
        }

        return new ShortestPath.Success<>(path);
    }

}
