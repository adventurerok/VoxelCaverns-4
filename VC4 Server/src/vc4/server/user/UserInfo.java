package vc4.server.user;

import vc4.api.server.Group;
import vc4.api.yaml.YamlMap;
import vc4.impl.permissions.ImplPermissionGroup;

public class UserInfo {

	private String chatName = "genericUser";
	private String groupName = "default";
	private ImplPermissionGroup permissions;
	
	public UserInfo(String name){
		chatName = name;
	}
	
	public UserInfo(YamlMap map){
		if(map.hasKey("name")) chatName = map.getString("name");
		if(map.hasKey("group")) groupName = map.getString("group");
		if(map.hasKey("permissions")) permissions = new ImplPermissionGroup(map.getSubMap("permissions"));
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public String getChatName() {
		return chatName;
	}
	
	public ImplPermissionGroup getPermissions() {
		return permissions;
	}
	
	public Group getGroup(){
		return UserManager.getGroup(groupName);
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public YamlMap toYaml(){
		YamlMap map = new YamlMap();
		map.setString("name", chatName);
		if(!groupName.equals("default")) map.setString("group", groupName);
		if(permissions != null){
			map.setSubMap("permissions", permissions.toYaml());
		}
		return map;
	}
	
	public int getPermission(String perm){
		if(permissions == null) return 0;
		return permissions.getSubPermission(perm);
	}
	
	public void changeChatName(String change, boolean any){
		if(change.equals(chatName)) return;
		int numIndex = change.length();
		for(int d = change.length() - 1; d > -1; --d){
			if(change.charAt(d) > 47 && change.charAt(d) < 58) continue;
			numIndex = d + 1;
			break;
		}
		String characters = change;
		int num = 0;
		if(numIndex != change.length()){
			characters = change.substring(0, numIndex);
			num = Integer.parseInt(change.substring(numIndex, change.length()));
		}
		UserManager.removeChatName(chatName);
		UserManager.setUserName(chatName, null);
		UserManager.addChatName(chatName = UserManager.generateChatName(characters, num));
		UserManager.setUserName(chatName, this);
	}
	
}
