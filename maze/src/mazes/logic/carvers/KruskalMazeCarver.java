package mazes.logic.carvers;


import graphs.EdgeWithData;
import graphs.minspantrees.MinimumSpanningTreeFinder;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeGraph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver extends MazeCarver {
    MinimumSpanningTreeFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder;
    private final Random rand;

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random();
    }

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder,
                             long seed) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(seed);
    }

    @Override
    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) {
        Set<Wall> toRemove = new HashSet<>();

        // convert (Wall) wall into (EdgeWithData) edge
        Collection<EdgeWithData<Room, Wall>> allEdges = new HashSet<>();
        for (Wall wall : walls) {
            allEdges.add(
                new EdgeWithData<>(wall.getRoom1(), wall.getRoom2(), rand.nextDouble(), wall));
        }

        for (EdgeWithData<Room, Wall> edge :
            this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(allEdges)).edges()) {
            toRemove.add(edge.data());
        }

        return toRemove;
    }
}
