/**
 * 
 */
package com.btsl.endersgame;

import java.util.List;

import android.opengl.GLES20;
import android.util.Log;

/**
 * @author Tom
 *
 */
public class ElementArrayBuffer extends DataBuffer<Integer> {
	
	protected int elemSize;
	
	public ElementArrayBuffer(List<Integer> data) {
		super(data, GLES20.GL_ELEMENT_ARRAY_BUFFER);
		if (dataType == GLES20.GL_UNSIGNED_BYTE) elemSize = Byte.SIZE / 8;
		else if (dataType == GLES20.GL_UNSIGNED_SHORT) elemSize = Short.SIZE / 8;
		else if (dataType == GLES20.GL_UNSIGNED_INT) elemSize = Integer.SIZE / 8;
		else Log.e("ElementArrayBuffer", "Invalid data type for ElementArrayBuffer: " + dataType);
	}
	
	public void draw(int mode) {
		bind();
		if (mode == GLES20.GL_LINE_LOOP) {
			// Draw wireframe
	        for (int i = 0; i < size * elemSize; i += 3 * elemSize) {
	            GLES20.glDrawElements(GLES20.GL_LINE_LOOP, 3, dataType, i);
	        }
		}
		else {
			GLES20.glDrawElements(mode, size, dataType, 0);
		}
	}
	
}
