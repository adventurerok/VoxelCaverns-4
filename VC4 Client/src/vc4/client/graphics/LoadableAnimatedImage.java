package vc4.client.graphics;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class LoadableAnimatedImage{

	URL image;
	URL txt;
	String texName;

	public LoadableAnimatedImage(URL url, String name) throws MalformedURLException {
		super();
		this.image = url;
		this.texName = name;
		String s = url.toString();
		s = s.substring(0, s.lastIndexOf("."));
		s = s + ".txt";
		txt = new URL(s);
	}

	public InputStream getImageStream() throws IOException {
		return image.openStream();
	}
	
	public InputStream getTxtStream() throws IOException{
		return txt.openStream();
	}


}