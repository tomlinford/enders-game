package com.btsl.endersgame;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

public class MainGLSurfaceView extends GLSurfaceView {
	
	// Java's producer/consumer data structure
	BlockingQueue<String> bq = new ArrayBlockingQueue<String>(5);

	public MainGLSurfaceView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		setDebugFlags(DEBUG_CHECK_GL_ERROR);
		setRenderer(new MainRenderer(context));
		setRenderMode(RENDERMODE_WHEN_DIRTY);
		new Thread(new ClientThread()).start();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		try {
			bq.put("" + e.getX() + " " + e.getY());
		} 
		catch (InterruptedException e1) {}
		return super.onTouchEvent(e);
	}
	
	/**
	 * Example usage of Client TCP
	 * @author Tom
	 *
	 */
	private class ClientThread implements Runnable {
		public void run() {
			Socket socket = null;
			try {
				// Private IP address of Tom's laptop
				InetAddress serverAddr = InetAddress.getByName("10.31.3.124");
				// specify port when creating the socket
				socket = new Socket(serverAddr, 1338);
				// the OutputStreamWriter can be wrapped with a BufferedWriter (and probably should be)
				PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
				while (true)
					out.println(bq.take());
			} catch (Exception e) {
				Log.e("ClientActivity", e.toString());
				
				// there's probably a better way to do this..
				try {
					if (socket != null)
						socket.close();
				} 
				catch (IOException e1) {}
			}
		}
	}

}
