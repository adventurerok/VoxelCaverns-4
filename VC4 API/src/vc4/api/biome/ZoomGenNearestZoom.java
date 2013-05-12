package vc4.api.biome;

import vc4.api.list.IntList;
import vc4.api.world.World;

public class ZoomGenNearestZoom extends ZoomGenerator {

	public ZoomGenNearestZoom(World world, ZoomGenerator parent) {
		super(world, parent);
	}

	@Override
	/*
	 * Zoom in on previous noise. 
	 * All maps store area off the side, for example a 18x18 map is 16x16, 
	 * with index 0 and 17 used for smoothing data
	 * This is works fine when generating limited size maps, but it doesn't work
	 * infinitely
	 * 
	 * Parameters: x position (in blocks), z position, zoom out, size of array to generate (size * size = arraysize)
	 */
	public int[] generate(long x, long z, int zoom, int size) {
		int parentZoom = ((size >> 1) + 3) & -2; //Size of parent output
		if(parentZoom == 18) parentZoom = 20;
		int[] parentOutput = parent.generate(x, z, zoom + 1, parentZoom);
		x = (x >> zoom) << zoom; //Get rid of insignificant bits. Is there a better way to do this?
		z = (z >> zoom) << zoom;
		int[] output = new int[size * size]; //Create output array
		for (int px = 0; px < size; ++px) {
			int qx = px / 2 + 2; //X index in parent array
			for (int pz = 0; pz < size; ++pz) {
				createRandom(x + ((px - 2) << (zoom)), z + ((pz - 2) << (zoom)), zoom); //Create a random for the position
				int qz = pz / 2 + 2; //Z index in parent array
				output[px * size + pz] = parentOutput[(qx) * parentZoom + (qz)]; //Zoom and smooth the output
			}
		}
		return output;
	}


	public int choose(IntList is) { //Choose a number out of the input
		if(is.size() == 3) return choose3(is);
		return is.get(rand.nextInt(is.size())); //Sometimes less than 3 numbers are inputted
	}
	
	public int choose3(IntList is){
		if(is.get(0) == is.get(2) || is.get(0) == is.get(1)) return is.get(0); //Is there a number that occurs more than once
		else if(is.get(1) == is.get(2)) return is.get(1); 
		else return is.get(rand.nextInt(is.size()));
	}
	

}
