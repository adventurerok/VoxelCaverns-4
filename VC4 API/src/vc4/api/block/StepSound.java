package vc4.api.block;

/**
 * Defines the sounds that are made when you move on a block, break a block or place a block
 * 
 * @author paul
 *
 */
public class StepSound {

	/**
	 * The link to the sound using resource directories
	 */
	public String sound;
	
	/**
	 * The volume that the sound will play at. 1.0 is the default
	 */
	public float volume;
	
	/**
	 * The pitch that the sound will play at. 1.0 is the default
	 */
	public float pitch;

	/**
	 * Creates a new step sound
	 * 
	 * @param sound The resourcedir link to the sound (e.g. npc/hurt)
	 * @param volume The volume of the sound. 1.0 is the default
	 * @param pitch The pitch of the sound. 1.0 is the default
	 */
	public StepSound(String sound, float volume, float pitch) {
		super();
		this.sound = sound;
		this.volume = volume;
		this.pitch = pitch;
	}
	
	/**
	 * Gets the breaking sound for this StepSound
	 * 
	 * @return The sound that is made when a block is broken
	 */
	public String getBreakSound(){
		return sound;
	}
	
	/**
	 * Gets the step sound for this StepSound
	 * 
	 * @return The sound that is made when a block is moved over
	 */
	public String getStepSound(){
		return sound;
	}
	
	/**
	 * Gets the place sound for this StepSound
	 * 
	 * @return The sound that is made when a block is placeed
	 */
	public String getPlaceSound(){
		return sound;
	}
	
	
}
