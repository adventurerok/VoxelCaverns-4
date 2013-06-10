package vc4.api.util;

public class Preconditions {

	
	public static void checkNotNull(Object check){
		if(check == null) throw new NullPointerException("Parameter is null");
	}
}
