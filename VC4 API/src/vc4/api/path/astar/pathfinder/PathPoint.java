package vc4.api.path.astar.pathfinder;

import vc4.api.entity.EntityLiving;
import vc4.api.vector.Vector3l;

public interface PathPoint {
    void addCallback(PathCallback callback);

    Vector3l getVector();

    public static interface PathCallback {
        void run(EntityLiving entity, Vector3l point);
    }
}