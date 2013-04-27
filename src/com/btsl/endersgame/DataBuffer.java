/**
 * 
 */
package com.btsl.endersgame;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
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
		}
		else if (data.get(0) instanceof Integer) {
			dataType = GLES20.GL_INT;
			IntBuffer ib = IntBuffer.wrap(toIntArray(data));
			GLES20.glBufferData(target, data.size() * 4, ib, GLES20.GL_STATIC_DRAW);
		}
		else {
			// 'instanceof' evaluates to false if data is null or empty
			dataType = 0;
			Log.e("DataBuffer", "Unsupported data type passed into DataBuffer constructor");
		}
		unbind();
	}
	
	/* Overloaded methods for converting Lists into arrays of primitives */
	
	private static <T> float[] toFloatArray(List<T> data) {
		float[] arrayData = new float[data.size()];
		for (int i = 0; i < data.size(); i++) 
			arrayData[i] = (Float)data.get(i);
		return arrayData;
	}
	
	private static <T> int[] toIntArray(List<T> data) {
		int[] arrayData = new int[data.size()];
		for (int i = 0; i < data.size(); i++) 
			arrayData[i] = (Integer)data.get(i);
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
