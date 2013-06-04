package vc4.vanilla.entity;

import org.jnbt.CompoundTag;

import vc4.api.entity.EntityLiving;
import vc4.api.entity.EntityPlayer;
import vc4.api.math.MathUtils;
import vc4.api.sound.Audio;
import vc4.api.world.World;
import vc4.vanilla.entity.ai.AIFreeNPCs;
import vc4.vanilla.entity.ai.AILookAtOthers;

public class EntityNpc extends EntityLiving {

	public enum NpcState{
		FREED,
		TRAPPED
	}
	
	String firstName = "Unnamed";
	String lastName =  "Villager";
	boolean man = true;
	double faction = 0; //100 <> -100
	NpcState old = NpcState.FREED;
	NpcState state = NpcState.FREED;
	
	int broadcastWait = 0;
	
	public void setMan(boolean man) {
		this.man = man;
	}
	
	public void setState(NpcState state) {
		this.state = state;
	}
	
	public EntityNpc(World world) {
		super(world);
		ais.put("free", new AIFreeNPCs(this, 15, 0.25));
		ais.put("look", new AILookAtOthers(this, 15));
		stateChanged();
	}

	@Override
	public String getName() {
		return "vanilla.villager";
	}
	
	public String getNameColor(){
		if(faction < -9.9999) return "{c:ff0000}";
		else if(faction > 9.9999) return "{c:00ff00}";
		else return "{c:f}";
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getSkin(){
		return man ? "villager" : "villagergirl";
	}
	
	@Override
	public void draw() {
		if(state == NpcState.TRAPPED){
			renderHumanModel(getSkin(), getNameColor() + firstName + " " + lastName, "gag"); 
		} else {
			renderHumanModel(getSkin(), getNameColor() + firstName + " " + lastName); 
		}
	}
	
	@Override
	public void onRightClick(EntityPlayer player) {
		if(state == NpcState.TRAPPED){
			free();
			String msg = formatChat() + "{l:villager.thanks" + (1 + rand.nextInt(2)) + "}";
			player.message(msg);
			faction++;
		}
	}
	
	public void free(){
		state = NpcState.FREED;
		Audio.playSound("npc/thanks", this, 1, 0.75f + rand.nextFloat() / 2);
	}
	
	public void addFaction(double amount){
		faction += amount;
		faction = MathUtils.clamp(faction, -100, 100);
	}
	
	@Override
	public CompoundTag getSaveCompound() {
		CompoundTag tag = super.getSaveCompound();
		tag.setString("firstName", firstName);
		tag.setString("lastName", lastName);
		tag.setBoolean("man", man);
		tag.setDouble("faction", faction);
		tag.setInt("state", state.ordinal());
		return tag;
	}
	
	@Override
	public void loadSaveCompound(CompoundTag tag) {
		super.loadSaveCompound(tag);
		firstName = tag.getString("firstName", "Unnamed");
		lastName = tag.getString("lastName", "Villager");
		man = tag.getBoolean("man", true);
		faction = tag.getDouble("faction", 0);
		old = state = NpcState.values()[tag.getInt("state", 0)];
		stateChanged();
	}
	
	@Override
	public void update() {
		if(broadcastWait == 0){
			broadcastMessage();
			broadcastWait = 400 + rand.nextInt(1800);
		} else broadcastWait--;
		if(old != state){
			stateChanged();
			old = state;
		}
		super.update();
	}
	
	protected void stateChanged(){
		ais.get("free").setDisabled(state == NpcState.TRAPPED);
	}

	public void broadcastMessage() {
		if(state == NpcState.TRAPPED){
			String msg = formatChat() + "{l:villager.trapped" + (1 + rand.nextInt(2)) + "}";
			world.broadcast(msg, position, 32);
			Audio.playSound("npc/help", this, 1, 0.75f + rand.nextFloat() / 2);
		}
	}
	
	public NpcState getState() {
		return state;
	}
	
	public String formatChat(String msg){
		return "<"+ firstName + " " + lastName + ">: " + msg;
	}
	
	public String formatChat(){
		return formatChat("");
	}

}
