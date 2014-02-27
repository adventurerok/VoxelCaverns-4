package vc4.api.server;

import vc4.api.graphics.GraphicsInfo;
import vc4.api.util.OS;

public interface ClientInfo {

	public OS getOs();

	public String getJavaVersion();

	public String getTimeZone();

	public String getVersion();

	public String getClientName();

	public GraphicsInfo getGraphicsInfo();

}
