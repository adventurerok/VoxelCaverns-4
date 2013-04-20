package org.jnbt;

public class BooleanTag extends Tag {

	private boolean value;
	
	public BooleanTag(String name) {
		super(name);
	}
	
	

	public BooleanTag(String name, boolean value) {
		super(name);
		this.value = value;
	}



	@Override
	public Boolean getValue() {
		return value;
	}
	
	@Override
	public void setValue(Object o) {
		if(o instanceof Boolean){
			value = (boolean) o;
		}
		
	}

	@Override
	public String toString() {
		String name = getName();
		String append = "";
		if(name != null && !name.equals("")) {
			append = "(\"" + this.getName() + "\")";
		}
		return "TAG_Boolean" + append + ": " + value;
	}

}
