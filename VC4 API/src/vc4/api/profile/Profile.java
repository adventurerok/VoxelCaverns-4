package vc4.api.profile;

import java.util.*;

public class Profile {


    private final List<String> nameList = new ArrayList<String>();

    private final List<Long> timeList = new ArrayList<Long>();

    public boolean enabled = true; //Can enable or disable for threads

    private String section = "";

    private final Map<String, Long> times = new HashMap<String, Long>();
    
    public void clear(){
    	times.clear();
    	nameList.clear();
    	section = "";
    }
	
	public void start(String task){
		if (this.enabled)
        {
            if (this.section.length() > 0)
            {
                this.section = this.section + ".";
            }

            this.section = this.section + task;
            this.nameList.add(this.section);
            this.timeList.add(Long.valueOf(System.nanoTime()));
        }
	}
	
	public void stop(){
		if (this.enabled)
        {
            long time = System.nanoTime();
            long oldTime = this.timeList.remove(this.timeList.size() - 1).longValue();
            this.nameList.remove(this.nameList.size() - 1);
            long diff = time - oldTime;

            if (this.times.containsKey(this.section))
            {
                this.times.put(this.section, Long.valueOf(this.times.get(this.section).longValue() + diff));
            }
            else
            {
                this.times.put(this.section, Long.valueOf(diff));
            }

            if (diff > 100L * 1000000L)
            {
                System.out.println("Section: " + section + " took to long (" + (diff / 1000000) + "ms)");
            }

            this.section = !this.nameList.isEmpty() ? this.nameList.get(this.nameList.size() - 1) : "";
        }
	}
	
	public void stopStart(String task){
		stop();
		start(task);
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
}
