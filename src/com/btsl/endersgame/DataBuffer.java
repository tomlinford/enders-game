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
	
	final int dataType;

	public DataBuffer(List<T> data, int target) {
		super(genBuffer(), target);
		if (data.isEmpty()) {
			dataType = 0;
			return;
		}
		
		if (data.get(0) instanceof Float) {
			float[] arrayData = new float[data.size()];
			for (int i = 0; i < arrayData.length; i++) {
				arrayData[i] = (Float) data.get(i);
			}
			FloatBuffer fb = FloatBuffer.wrap(arrayData);
			GLES20.glBufferData(target, arrayData.length * 4, fb, GLES20.GL_STATIC_DRAW);
			dataType = GLES20.GL_FLOAT;
		} else if (data.get(0) instanceof Integer) {
			int[] arrayData = new int[data.size()];
			for (int i = 0; i < arrayData.length; i++) {
				arrayData[i] = (Integer) data.get(i);
			}
			IntBuffer ib = IntBuffer.wrap(arrayData);
			GLES20.glBufferData(target, arrayData.length * 4, ib, GLES20.GL_STATIC_DRAW);
			dataType = GLES20.GL_INT;
		} else {
			Log.e("DataBuffer", "Unknown dataType passed into DataBuffer constructor");
			dataType = 0;
		}
	}

	@Override
	public void bind() {
		GLES20.glBindBuffer(target, id);
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
