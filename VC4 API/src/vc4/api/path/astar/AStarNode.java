package vc4.api.path.astar;

import java.util.*;


/**
 * 
 * @author CitizensNPCs
 *
 */
public abstract class AStarNode implements Comparable<AStarNode> {
    float f, g, h;
    AStarNode parent;

    List<AStarNode> parents;

    public abstract Plan buildPlan();

    @Override
    public int compareTo(AStarNode other) {
        return Float.compare(f, other.f);
    }

    public abstract Iterable<AStarNode> getNeighbours();

    protected AStarNode getParent() {
        return parent;
    }

    @SuppressWarnings("unchecked")
    protected <T extends AStarNode> Iterable<T> getParents() {
        if (parents != null)
            return (Iterable<T>) parents;
        parents = new ArrayList<>();
        AStarNode start = this;
        while (start != null) {
            parents.add(start);
            start = start.parent;
        }
        Collections.reverse(parents);
        return (Iterable<T>) parents;
    }

    protected float getPathCost() {
        return f;
    }
}