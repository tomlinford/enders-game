/**
 * 
 */
package com.btsl.endersgame;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.List;

import android.opengl.GLES20;
import android.util.Log;

/**
 * @author Tom
 *
 */
public abstract class DataBuffer<T> extends AbstractBuffer {

	public final int dataType;

	public DataBuffer(List<T> data, int target) {
		super(genBuffer(), target, data.size());

		bind();
		if (data.get(0) instanceof Float) {
			dataType = GLES20.GL_FLOAT;
			FloatBuffer fb = FloatBuffer.wrap(toFloatArray(data));
			GLES20.glBufferData(target, data.size() * 4, fb, GLES20.GL_STATIC_DRAW);
		} else if (data.get(0) instanceof Integer) {
			
			// use whatever data type will use the least amount of space in memory
			if (size < Byte.MAX_VALUE) {
				dataType = GLES20.GL_UNSIGNED_BYTE;
				ByteBuffer bb = ByteBuffer.wrap(toByteArray(data));
				GLES20.glBufferData(target, data.size(), bb, GLES20.GL_STATIC_DRAW);
			} else if (size < Short.MAX_VALUE) {
				dataType = GLES20.GL_UNSIGNED_SHORT;
				ShortBuffer sb = ShortBuffer.wrap(toShortArray(data));
				GLES20.glBufferData(target, data.size() * 2, sb, GLES20.GL_STATIC_DRAW);
			} else { // assume it's less than Integer.MAX_VALUE
				dataType = GLES20.GL_UNSIGNED_INT;
				IntBuffer ib = IntBuffer.wrap(toIntArray(data));
				GLES20.glBufferData(target, data.size() * 4, ib, GLES20.GL_STATIC_DRAW);
			}
		} else {
			// 'instanceof' evaluates to false if data is null or empty
			dataType = 0;
			Log.e("DataBuffer", "Unsupported data type passed into DataBuffer constructor");
		}
		unbind();
	}

	/* Overloaded methods for converting Lists into arrays of primitives */

	private static <T> float[] toFloatArray(List<T> data) {
		float[] arrayData = new float[data.size()];

		// using this way of building the for loop uses iterators, so if the List
		// is implemented as LinkedList, the get() method wouldn't take 0(n) time
		int i = 0;
		for (T elem : data) arrayData[i++] = (Float) elem;
		return arrayData;
	}

	private static <T> byte[] toByteArray(List<T> data) {
		byte[] arrayData = new byte[data.size()];
		int i = 0;
		for (T elem : data) {
			arrayData[i++] = ((Integer) elem).byteValue();
		}
		return arrayData;
	}

	private static <T> short[] toShortArray(List<T> data) {
		short[] arrayData = new short[data.size()];
		int i = 0;
		for (T elem : data) arrayData[i++] = (Short) elem;
		return arrayData;
	}

	private static <T> int[] toIntArray(List<T> data) {
		int[] arrayData = new int[data.size()];
		int i = 0;
		for (T elem : data) arrayData[i++] = ((Integer) elem).shortValue();
		return arrayData;
	}

	/**
	 * Generate a buffer for the DataBuffer
	 * @return
	 */
	private static int genBuffer() {
		int[] buf = new int[1];
		GLES20.glGenBuffers(1, buf, 0);
		return buf[0];
	}

}
