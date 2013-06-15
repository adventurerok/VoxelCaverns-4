package vc4.api.path.astar.pathfinder;

import java.util.*;

import vc4.api.path.astar.Agent;
import vc4.api.path.astar.Plan;
import vc4.api.path.astar.pathfinder.PathPoint.PathCallback;
import vc4.api.vector.Vector3l;

public class Path implements Plan {
    private int index = 0;
    private final PathEntry[] path;

    Path(Iterable<VectorNode> unfiltered) {
        this.path = cull(unfiltered);
    }

    private PathEntry[] cull(Iterable<VectorNode> unfiltered) {
        // possibly expose cullability in an API
        List<PathEntry> path = new ArrayList<>();
        Vector3l last = null;
        for (VectorNode node : unfiltered) {
            if (node.callbacks != null)
                continue;
            Vector3l vector = node.location;
            if (last != null && vector.y == last.y) {
//                if (node.blockSource.getMaterialAt(vector) == 0
//                        && node.blockSource.getMaterialAt(last) == 0) {
//                    continue;
//                }
            }
            last = vector;
            path.add(new PathEntry(vector, node.callbacks));
        }
        return path.toArray(new PathEntry[path.size()]);
    }

    public Vector3l getCurrentVector() {
        return path[index].vector;
    }

    @Override
    public boolean isComplete() {
        return index >= path.length;
    }

    @Override
    public String toString() {
        return Arrays.toString(path);
    }

    @Override
    public void update(Agent agent) {
        if (isComplete())
            return;
        PathEntry entry = path[index];
        if (entry.hasCallbacks()) {
//            NPC npc = (NPC) agent;
//            Block block = entry.getBlockUsingWorld(npc.getBukkitEntity().getWorld());
//            for (PathCallback callback : entry.callbacks) {
//                callback.run(npc, block);
//            }
        }
        ++index;
    }

    private static class PathEntry {
        final Iterable<PathCallback> callbacks;
        final Vector3l vector;

        private PathEntry(Vector3l vector, List<PathCallback> callbacks) {
            this.vector = vector;
            this.callbacks = callbacks;
        }


        private boolean hasCallbacks() {
            return callbacks != null;
        }

        @Override
        public String toString() {
            return vector.toString();
        }
    }
}