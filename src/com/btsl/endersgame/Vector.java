package com.btsl.endersgame;

public class Vector {
	
	/**
	 * Set the values in vector v from the provided x, y, z
	 * @param v
	 * @param offset
	 * @param x
	 * @param y
	 * @param z
	 */
	public static void setV3(float[]v, int offset, float x, float y, float z) {
		v[offset] = x;
		v[offset + 1] = y;
		v[offset + 2] = z;
	}
	
	/**
	 * set all elements in vector v to have the value s
	 * @param v
	 * @param offset
	 * @param s
	 */
	public static void setV3(float[]v, int offset, float s) {
		setV3(v, offset, s, s, s);
	}
	
	/**
	 * Copy over the vector from to v
	 * @param v
	 * @param offset
	 * @param from
	 * @param fromOffset
	 */
	public static void setV3(float[] v, int offset, float[] from, int fromOffset) {
		setV3(v, offset, from[fromOffset], from[fromOffset + 1], from[fromOffset + 2]);
	}
	
	/**
	 * Dot product of two vector3s
	 * @param v1
	 * @param offset1
	 * @param v2
	 * @param offset2
	 * @return
	 */
	public static float dotV3(float[] v1, int offset1, float[] v2, int offset2) {
		float total = 0f;
		for (int i = 0; i < 3; i++) total += v1[offset1 + i] * v2[offset2 + i];
		return total;
	}
	
	/**
	 * Length of vector3
	 * @param v
	 * @param offset
	 * @return
	 */
	public static float lengthV3(float[] v, int offset) {
		return (float) Math.sqrt(dotV3(v, offset, v, offset));
	}
	
	/**
	 * Normalize the vector v
	 * @param v
	 * @param offset
	 */
	public static void normalizeV3(float[] v, int offset) {
		float len = lengthV3(v, offset);
		for (int i = 0; i < 3; i++) v[offset + i] /= len;
	}
	
	/**
	 * Cross the vectors l and r and store the result in res
	 * @param res
	 * @param resOffset
	 * @param l
	 * @param lOffset
	 * @param r
	 * @param rOffset
	 */
	public static void crossV3(float[] res, int resOffset, float[] l, int lOffset, float[] r, int rOffset) {
		res[resOffset] = l[lOffset + 1] * r[rOffset + 2] - l[lOffset + 2] * r[rOffset + 1];
		res[resOffset + 1] = l[lOffset + 2] * r[rOffset] - l[lOffset] * r[rOffset + 2];
		res[resOffset + 2] = l[lOffset] * r[rOffset + 1] - l[lOffset + 1] * r[rOffset];
	}
	
	/**
	 * Scale the vector v by a factor of s
	 * @param v
	 * @param offset
	 * @param s
	 */
	public static void scaleV3(float[] v, int offset, float s) {
		for (int i = 0; i < 3; i++) v[offset + i] *= s;
	}
	
	/**
	 * Add the vector toAdd to the vector v
	 * @param v
	 * @param offset
	 * @param toAdd
	 * @param taOffset
	 */
	public static void addV3(float[] v, int offset, float[] toAdd, int taOffset) {
		for (int i = 0; i < 3; i++) v[offset + i] += toAdd[taOffset + i];
	}

}
