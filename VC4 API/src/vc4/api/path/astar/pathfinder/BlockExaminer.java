package vc4.api.path.astar.pathfinder;

/**
 * 
 * @author CitizensNPCs
 * 
 */
public interface BlockExaminer {
	float getCost(BlockSource source, PathPoint point);

	boolean isPassable(BlockSource source, PathPoint point);
}