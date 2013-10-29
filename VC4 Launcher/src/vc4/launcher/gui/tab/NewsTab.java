package vc4.launcher.gui.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.net.URI;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;

public class NewsTab extends JPanel {
	public NewsTab() {
		setLayout(new BorderLayout(0, 0));
		
		final JEditorPane news = new JEditorPane();
		news.setEditorKit(new HTMLEditorKit());
		news.setText("<html><body><font color=\"#0000ff\"><br><br><br><br><br><br><br><center>Loading update feed...</center></font></body></html>");
		news.addHyperlinkListener(new HyperlinkListener() {
	        @Override
			public void hyperlinkUpdate(HyperlinkEvent he) {
	          if (he.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
	            try {
	            	final String url = he.getURL().toString();
	            	if(url.contains("blog.voxelcaverns.org.uk/page")){
	            		Thread t = new Thread("Load URL"){
	            			@Override
	            			public void run() {
	            				try{
	            					news.setPage(url);
	            					
	            				} catch(Exception e){
	            					news.setText("<html><body><font color=\"#0000ff\"><br><br><br><br><br><br><br><center>Failed to load news feed:<br>" + e.toString() + "<br>Please check your Internet connection</center></font></body></html>");
	            				}
	            			}
	            		};
	            		t.start();
	            	}
	            	else openLink(he.getURL().toURI());
	            } catch (Exception e) {
	              e.printStackTrace();
	            }
	        }
	      });
		Thread t = new Thread("Load URL"){
			@Override
			public void run() {
				try{
					news.setPage("http://blog.voxelcaverns.org.uk/tagged/updates");
					
				} catch(Exception e){
					news.setText("<html><body><font color=\"#0000ff\"><br><br><br><br><br><br><br><center>Failed to load news feed:<br>" + e.toString() + "<br>Please check your Internet connection</center></font></body></html>");
				}
			}
		};
		t.start();
		news.setBackground(Color.lightGray);
		news.setEditable(false);
		JScrollPane scroller = new JScrollPane(news);
		add(scroller);
	}
	
	public void openLink(URI uri) {
		try {
			Object o = Class.forName("java.awt.Desktop").getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
			o.getClass().getMethod("browse", new Class[] { URI.class }).invoke(o, new Object[] { uri });
		} catch (Throwable e) {
			System.out.println("Failed to open link " + uri.toString());
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8629244080721280459L;

}
