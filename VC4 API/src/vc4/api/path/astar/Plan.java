package vc4.api.path.astar;

/**
 * 
 * @author Citizens
 * 
 */
public interface Plan {
	boolean isComplete();

	void update(Agent agent);
}