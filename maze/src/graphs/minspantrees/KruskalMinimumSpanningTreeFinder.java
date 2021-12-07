package graphs.minspantrees;

import disjointsets.DisjointSets;
// import disjointsets.QuickFindDisjointSets;
import disjointsets.UnionBySizeCompressingDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        //return new QuickFindDisjointSets<>();
        /*
        Disable the line above and enable the one below after you've finished implementing
        your `UnionBySizeCompressingDisjointSets`.
         */
        return new UnionBySizeCompressingDisjointSets<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        Set<E> mst = new HashSet<>();

        if (graph.allEdges().size() == 0 && graph.allVertices().size() < 2) {
            return new MinimumSpanningTree.Success<>(mst);
        } else if (graph.allEdges().size() == 0 || graph.allVertices().size() == 0) {
            return new MinimumSpanningTree.Failure<>();
        }

        List<E> edges = new ArrayList<>(graph.allEdges());
        edges.sort(Comparator.comparingDouble(E::weight));

        DisjointSets<V> disjointSets = createDisjointSets();

        int count = graph.allVertices().size();
        for (V u : graph.allVertices()) {
            disjointSets.makeSet(u);
        }

        Iterator<E> itr = edges.iterator();
        while (count > 1 && itr.hasNext()) {
            E e = itr.next();
            if (disjointSets.findSet(e.from()) != disjointSets.findSet(e.to())) {
                mst.add(e);
                disjointSets.union(e.from(), e.to());
                count--;
            }
        }

        if (count > 1) {
            return new MinimumSpanningTree.Failure<>();
        } else {
            return new MinimumSpanningTree.Success<>(mst);
        }
    }
}
