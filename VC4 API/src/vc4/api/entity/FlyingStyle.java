package vc4.api.entity;

public enum FlyingStyle {

	
	WALK,
	NOCLIP,
	FLY;

	public FlyingStyle next() {
		if(this == WALK) return NOCLIP;
		if(this == FLY) return WALK;
		return FLY;
	}
}
