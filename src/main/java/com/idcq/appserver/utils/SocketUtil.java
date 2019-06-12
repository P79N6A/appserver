package com.idcq.appserver.utils;

import java.io.IOException;
import java.net.Socket;

public class SocketUtil {
	class Test extends Thread{
		@Override
		public void run() {
			Socket socket = null;
			String re = "";
			try {
				socket = new Socket("", 6081);
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				
			}
		}
	}
}
