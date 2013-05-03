package com.btsl.endersgame;

import android.opengl.Matrix;

public class Camera {
	
	/**
	 * View matrix of camera, stored here for simplicity
	 */
	private static float[] view = new float[16];
	
	/**
	 * Location of camera
	 */
	private static float[] loc = new float[3];
	
	/**
	 * Orientation of camera
	 */
	private static float[] orientation = new float[16];
	
	/**
	 * Direction vector
	 */
	private static float[] direction = new float[3];
	
	/**
	 * Up vector
	 */
	private static float[] up = new float[3];
	
	/**
	 * right vector
	 */
	private static float[] right = new float[3];
	
	/**
	 * Movement vector at a given time
	 */
	private static float[] movement = new float[3];
	
	public static void init() {
		Matrix.setIdentityM(view, 0);
		loc[0] = 5f;
		Quat.setIdentityQ(orientation, 0);
		direction[0] = -1f;
		up[1] = 1f;
		right[2] = -1f;
		calculateView();
	}
	
	/**
	 * Temporary vector
	 */
	private static float[] temp = new float[3];
	
	/**
	 * Move forward an amount of d
	 * @param d
	 */
	public static void moveForward(float d) {
		Vector.setV3(direction, 0, -1, 0, 0);
		Quat.rotateQV3(orientation, 0, direction, 0);
		Vector.normalizeV3(direction, 0);
		Vector.setV3(up, 0, 0, 1, 0);
		Quat.rotateQV3(orientation, 0, up, 0);
		Vector.normalizeV3(up, 0);

		Vector.setV3(movement, 0, 0);
		Vector.setV3(temp, 0, direction, 0);
		Vector.scaleV3(temp, 0, d);
		Vector.addV3(movement, 0, temp, 0);
		Vector.addV3(loc, 0, movement, 0);
		calculateView();
	}
	
	/**
	 * Gets current view matrix
	 * @return
	 */
	public static float[] getView() {
		return view;
	}
	
	/**
	 * Helper that calculates the current view matrix
	 */
	public static void calculateView() {
		Vector.setV3(temp, 0, loc, 0);
		Vector.addV3(temp, 0, direction, 0);
		Matrix.setLookAtM(view, 0, loc[0], loc[1], loc[2], temp[0], temp[1], temp[2], up[0], up[1], up[2]);
	}

}
