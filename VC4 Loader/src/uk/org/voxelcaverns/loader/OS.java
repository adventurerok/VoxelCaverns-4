package uk.org.voxelcaverns.loader;



public enum OS {

	WINDOWS,
	MAC,
	SOLARIS,
	LINUX,
	UNKNOWN;
	
	public static OS getOs()
    {
        String s = System.getProperty("os.name").toLowerCase();
		if (s.contains("win")) return WINDOWS;
		if (s.contains("mac")) return MAC;
		if (s.contains("solaris")) return SOLARIS;
		if (s.contains("sunos")) return SOLARIS;
        if (s.contains("linux")) return LINUX;
        if (s.contains("unix")) return LINUX;
        else return UNKNOWN;
    }
}
