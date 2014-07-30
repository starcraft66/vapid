package me.pyr0byte.vapid;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class HttpGet implements Runnable {

	String urlS;
	
	public HttpGet(String url) {
		this.urlS = url;
	}
	
	public void run() {
		
		try {
			URL url = new URL(urlS);
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();
		} catch (Exception e) {
		
		}
		
	}
}
