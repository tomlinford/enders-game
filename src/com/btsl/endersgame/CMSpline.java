package com.btsl.endersgame;

import android.opengl.Matrix;

/**
 * @author bhnascar
 *
 */
public class CMSpline {
	
	// Single dimension control points
	float[] c = new float[4];
	
	// 3 dimension control points, 1 set per axis.
	float[] x_c = new float[4];
	float[] y_c = new float[4];
	float[] z_c = new float[4];
	
	// Column major
	public static float[] weights = {
	    0.0f, -0.5f, 1.0f, -0.5f,
	    1.0f, 0.0f, -2.5f, 1.5f,
	    0.0f, 0.5f, 2.0f, -1.5f,
	    0.0f, 0.0f, -0.5f, 0.5f
	};
	
	public CMSpline(float p1, float p2, float p3, float p4) {
		genConstants(p1, p2, p3, p4);
	}
	
	public CMSpline(float[] p1, float[] p2, float[] p3, float[] p4) {
		genConstants(p1, p2, p3, p4);
	}
	
	/**
	 * Generates interpolation constants for the given 1D positions
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param p4
	 */
	public void genConstants(float p1, float p2, float p3, float p4) {
		Matrix.multiplyMV(c, 0, weights, 0, new float[]{p1, p2, p3, p4}, 0);
	}
	
	/**
	 * Generates interpolation constants for the given 3D positions.
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param p4
	 */
	public void genConstants(float[] p1, float[] p2, float[] p3, float[] p4) {
		Matrix.multiplyMV(x_c, 0, weights, 0, new float[]{p1[0], p2[0], p3[0], p4[0]}, 0);
		Matrix.multiplyMV(y_c, 0, weights, 0, new float[]{p1[1], p2[1], p3[1], p4[1]}, 0);
		Matrix.multiplyMV(z_c, 0, weights, 0, new float[]{p1[2], p2[2], p3[2], p4[2]}, 0);
	}
	
	/**
	 * Returns a interpolated position in 1D, given four control values.
	 * @param u
	 * @return A interpolated position in 1D
	 */
	public float evaluate(float u) {
		return (float)(c[0] + c[1] * u + c[2] * Math.pow(u, 2) + c[3] * Math.pow(u, 3));
	}

	/**
	 * Returns a interpolated position in 3D space. Should only be called
	 * on a CMSpline created with 3D control points.
	 * @param u
	 * @return A interpolated position in 3D
	 */
	public float[] evaluate_vec(float u) {
		float[] interp = new float[3];
		
		// Interpolate x
		interp[0] = (float)(x_c[0] + x_c[1] * u + x_c[2] * Math.pow(u, 2) + x_c[3] * Math.pow(u, 3));
		
		// Interpolate y
		interp[1] = (float)(y_c[0] + y_c[1] * u + y_c[2] * Math.pow(u, 2) + y_c[3] * Math.pow(u, 3));
		
		// Interpolate z
		interp[2] = (float)(z_c[0] + z_c[1] * u + z_c[2] * Math.pow(u, 2) + z_c[3] * Math.pow(u, 3));
		
		return interp;
	}
	
	/**
	 * Evaluates the rotation
	 * @param q1
	 * @param q1Offset
	 * @param q2
	 * @param q2Offset
	 * @param u
	 * @return
	 */
	public float[] evaluate_quat(float[] q1, int q1Offset, float[] q2, int q2Offset, float u) {
		float[] q = new float[4];
		
		float[] nq1 = new float[4];
		float[] nq2 = new float[4];
		Quat.setFromQuatQ(nq1, 0, q1, q1Offset);
		Quat.setFromQuatQ(nq2, 0, q2, q1Offset);
		
		Quat.normalizeQ(nq1, q1Offset);
		Quat.normalizeQ(nq2, q2Offset);
		float dotted = Quat.dotQ(nq1, 0, nq2, 0);
		float alpha = (float) Math.acos(dotted);
		
		Quat.slerp(q, 0, q1, q1Offset, q2, q2Offset, u, alpha);
		Quat.normalizeQ(q, 0);
		return q;
	}
	
}
