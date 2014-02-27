package vc4.api.path;

import vc4.api.vector.Vector3d;

public interface PathStrategy {
	void clearCancelReason();

	CancelReason getCancelReason();

	Vector3d getTargetAsVector();

	TargetType getTargetType();

	void stop();

	boolean update();
}
