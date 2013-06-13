package vc4.api.path;

import vc4.api.path.astar.AStarMachine;
import vc4.api.path.astar.pathfinder.Path;
import vc4.api.path.astar.pathfinder.VectorNode;

public class AStarNavigator implements Navigator {

	AStarMachine<VectorNode, Path> machine;
}
