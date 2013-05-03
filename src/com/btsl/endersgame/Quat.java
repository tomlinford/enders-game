/**
 * 
 */
package com.btsl.endersgame;

/**
 * @author Tom
 *
 */
public class Quat {
	
	/**
	 * Set q to be the identity quaternion
	 * @param q
	 * @param offset
	 */
	public static void setIdentityQ(float[] q, int offset) {
		q[offset] = 1;
		q[offset + 1] = 0;
		q[offset + 2] = 0;
		q[offset + 3] = 0;
	}
	
	/**
	 * Set q to be the quaternion derived from the vector v
	 * @param q
	 * @param offsetQ
	 * @param v
	 * @param offsetV
	 */
	public static void setFromVectorQ(float[] q, int offsetQ, float[] v, int offsetV) {
		q[offsetQ] = 0;
		q[offsetQ + 1] = v[offsetV];
		q[offsetQ + 2] = v[offsetV + 1];
		q[offsetQ + 3] = v[offsetV + 2];
	}
	
	/**
	 * Copy over to res from q
	 * @param res
	 * @param resOffset
	 * @param q
	 * @param offset
	 */
	public static void setFromQuatQ(float[] res, int resOffset, float[] q, int offset) {
		res[resOffset] = q[offset];
		res[resOffset + 1] = q[offset + 1];
		res[resOffset + 2] = q[offset + 2];
		res[resOffset + 3] = q[offset + 3];
	}
	
	/**
	 * Negate the quaternion q
	 * @param q
	 * @param offset
	 */
	public static void negateQ(float[] q, int offset) {
		q[offset] = -q[offset];
		q[offset + 1] = -q[offset + 1];
		q[offset + 2] = -q[offset + 2];
		q[offset + 3] = -q[offset + 3];
	}
	
	/**
	 * Take the conjugate of q and store the result in res
	 * @param res
	 * @param resOffset
	 * @param q
	 * @param offset
	 */
	public static void conjugateQ(float[] res, int resOffset, float[] q, int offset) {
		res[resOffset] = q[offset];
		res[resOffset + 1] = -q[offset + 1];
		res[resOffset + 2] = -q[offset + 2];
		res[resOffset + 3] = -q[offset + 3];
	}
	
	/**
	 * Take the dot product of q1 and q2
	 * @param q1
	 * @param offset1
	 * @param q2
	 * @param offset2
	 * @return
	 */
	public static float dotQ(float[] q1, int offset1, float[] q2, int offset2) {
		return q1[offset1] * q2[offset2] +
				q1[offset1 + 1] * q2[offset2 + 1] +
				q1[offset1 + 2] * q2[offset2 + 2];
	}
	
	/**
	 * Return the length of q
	 * @param q
	 * @param offset
	 * @return
	 */
	public static float lengthQ(float[] q, int offset) {
		return (float) Math.sqrt(dotQ(q, offset, q, offset));
	}
	
	/**
	 * Take the cross product of lhs and rhs and store the result in res
	 * @param res
	 * @param resOffset
	 * @param lhs
	 * @param lhsOffset
	 * @param rhs
	 * @param rhsOffset
	 */
	public static void crossQ(float[] res, int resOffset, float[] lhs, int lhsOffset, float[] rhs, int rhsOffset) {
	    res[resOffset] = lhs[lhsOffset] * rhs[rhsOffset] - lhs[lhsOffset + 1] * rhs[rhsOffset + 1] - lhs[lhsOffset + 2] * rhs[rhsOffset + 2] - lhs[lhsOffset + 3] * rhs[rhsOffset + 3];
	    res[resOffset + 1] = lhs[lhsOffset] * rhs[rhsOffset + 1] + lhs[lhsOffset + 1] * rhs[rhsOffset] + lhs[lhsOffset + 2] * rhs[rhsOffset + 3] - lhs[lhsOffset + 3] * rhs[rhsOffset + 2];
	    res[resOffset + 2] = lhs[lhsOffset] * rhs[rhsOffset + 2] - lhs[lhsOffset + 1] * rhs[rhsOffset + 3] + lhs[lhsOffset + 2] * rhs[rhsOffset] + lhs[lhsOffset + 3] * rhs[rhsOffset + 1];
	    res[resOffset + 3] = lhs[lhsOffset] * rhs[rhsOffset + 3] + lhs[lhsOffset + 1] * rhs[rhsOffset + 2] - lhs[lhsOffset + 2] * rhs[rhsOffset + 1] + lhs[lhsOffset + 3] * rhs[rhsOffset];
	}
	
	/**
	 * Normalizes the quaternion q
	 * @param q
	 * @param offset
	 */
	public static void normalizeQ(float[] q, int offset) {
		float length = lengthQ(q, offset);
		if (length <= 0.0f) {
			setIdentityQ(q, offset);
		} else {
			q[offset] /= length;
			q[offset + 1] /= length;
			q[offset + 2] /= length;
			q[offset + 3] /= length;
		}
	}
	
	/**
	 * Extra array for rotateQV3's usage
	 */
	private static float[] rotateExtras = new float[12];
	
	/**
	 * Rotate the vector3 v by q and store the result back into v
	 * @param q
	 * @param qOffset
	 * @param v
	 * @param vOffset
	 */
	public synchronized static void rotateQV3(float[] q, int qOffset, float[] v, int vOffset) {
		setFromVectorQ(rotateExtras, 0, v, vOffset);
		crossQ(rotateExtras, 4, q, qOffset, rotateExtras, 0);
		conjugateQ(rotateExtras, 8, q, qOffset);
		crossQ(rotateExtras, 0, rotateExtras, 4, rotateExtras, 8);
		v[vOffset] = rotateExtras[1];
		v[vOffset + 1] = rotateExtras[2];
		v[vOffset + 2] = rotateExtras[3];
	}

}
