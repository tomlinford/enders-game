package com.btsl.endersgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;

public class MainGLSurfaceView extends GLSurfaceView {
	
	// Java's producer/consumer data structure
	private BlockingQueue<String> bq = new ArrayBlockingQueue<String>(5);
	
	// later, we'll be able to call methods on renderer
	private MainRenderer renderer;

	public MainGLSurfaceView(Context context) {
		super(context);
		
		Camera.init();
		
		setEGLContextClientVersion(2);
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		setDebugFlags(DEBUG_CHECK_GL_ERROR);
		
		renderer = new MainRenderer(context);
		setRenderer(renderer);
//		setRenderMode(RENDERMODE_WHEN_DIRTY);
		
		new Thread(new ClientThread()).start();
	}
	
	/**
	 * Locations of where screen is being touched
	 */
	private float prevX, prevY;
	
	/**
	 * Max number of fingers on the screen for one swipe
	 */
	private int numPointers;
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		
		int action = MotionEventCompat.getActionMasked(e);
		
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			prevX = e.getX();
			prevY = e.getY();
			numPointers = e.getPointerCount();
			return true;
		case MotionEvent.ACTION_UP:
			numPointers = 0;
			return true;
		case MotionEvent.ACTION_MOVE:
			float x = e.getX();
			float y = e.getY();
			if (e.getPointerCount() != numPointers) {
				numPointers = e.getPointerCount();
			} else {
				// currently, this is set up to look around with one finger, move around
				// with two, and roll with three
				if (numPointers == 2) {
					Camera.move((prevY - y) / 100f, -(prevX - x) / 100f);
				} else if (numPointers == 1) {
					Camera.rotate((y - prevY) / 400f, (x - prevX) / 400f, 0f);
				} else if (numPointers == 3) {
					Camera.rotate(0f, 0f, (x - prevX) / 300f);
				}
				// re-render the scene
				requestRender();
			}
			prevX = x;
			prevY = y;
			return true;
		default:
			return super.onTouchEvent(e);
		}
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
//				PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
//				while (true)
//					out.println(bq.take());
				BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				Random rgen = new Random();
				for (String line = rd.readLine(); line != null; line = rd.readLine()) {
					if (line.trim().equals("subdivide")) {
						renderer.subdivideCube();
						requestRender();
					} else if (line.equals("flat")) {
						renderer.swapFlat();
						requestRender();
					} else if (line.split(" ")[0].equals("animate")) {
						Log.e("Animation", "Received animation command");
						String[] components = line.split(" ");
						long now = System.currentTimeMillis();
						for (int i = 1; i < components.length - 2; i += 3) {
							KeyFrame frame = new KeyFrame();
							frame.position[0] = Float.parseFloat(components[i]);
							frame.position[1] = Float.parseFloat(components[i + 1]);
							frame.position[2] = Float.parseFloat(components[i + 2]);
							frame.time = now  + (i/3 - 2) * 1000;
							frame.orientation[0] = 1.f;
							for (int j = 1; j < 4; j++) {
								frame.orientation[j] = (float) rgen.nextInt(100) / 100.f;
							}
							Quat.normalizeQ(frame.orientation, 0);
							renderer.keyFrames.add(frame);
						}
					} else if (line.equals("spotlight")) {
						renderer.swapRadius();
						requestRender();
					}
				}
			} catch (Exception e) {
				Log.e("ClientActivity", e.toString());
				
				// there's probably a better way to do this..
				try {
					if (socket != null) {
						socket.close();
						socket = null;
					}
				} catch (IOException e1) { }
				for (;;)
					try {
						bq.take();
					} catch (InterruptedException e1) {}
			} finally {
				if (socket != null)
					try {
						socket.close();
					} catch (IOException e) {}
			}
		}
	}

}
