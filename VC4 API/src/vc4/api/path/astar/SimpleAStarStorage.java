package vc4.api.path.astar;

import java.util.*;

import vc4.api.util.Supplier;


/**
 * 
 * @author CitizensNPCs
 *
 */
public class SimpleAStarStorage implements AStarStorage {
    private final Map<AStarNode, Float> closed = new HashMap<>();
    private final Map<AStarNode, Float> open = new HashMap<>();
    private final Queue<AStarNode> queue = new PriorityQueue<AStarNode>();

    @Override
    public void close(AStarNode node) {
        open.remove(node);
        closed.put(node, node.f);
    }

    @Override
    public AStarNode getBestNode() {
        return queue.peek();
    }

    @Override
    public void open(AStarNode node) {
        queue.offer(node);
        open.put(node, node.f);
        closed.remove(node);
    }

    @Override
    public AStarNode removeBestNode() {
        return queue.poll();
    }

    @Override
    public boolean shouldExamine(AStarNode neighbour) {
        Float openF = open.get(neighbour);
        if (openF != null && openF > neighbour.f) {
            open.remove(neighbour);
            openF = null;
        }
        Float closedF = closed.get(neighbour);
        if (closedF != null && closedF > neighbour.f) {
            closed.remove(neighbour);
            closedF = null;
        }
        return closedF == null && openF == null;
    }

    @Override
    public String toString() {
        return "SimpleAStarStorage [closed=" + closed + ", open=" + open + "]";
    }

    public static final Supplier<AStarStorage> FACTORY = new Supplier<AStarStorage>() {
        @Override
        public AStarStorage get() {
            return new SimpleAStarStorage();
        }
    };
}