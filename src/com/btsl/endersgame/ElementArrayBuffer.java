/**
 * 
 */
package com.btsl.endersgame;

import java.util.List;

import android.opengl.GLES20;

/**
 * @author Tom
 *
 */
public class ElementArrayBuffer extends DataBuffer<Integer> {
	
	public ElementArrayBuffer(List<Integer> data) {
		super(data, GLES20.GL_ELEMENT_ARRAY_BUFFER);
	}
	
	public void draw(int mode) {
		bind();
		if (mode == GLES20.GL_LINE_LOOP) {
			// Draw wireframe
	        for (int i = 0; i < size * (Short.SIZE / 8); i += 3 * (Short.SIZE / 8)) {
	            GLES20.glDrawElements(GLES20.GL_LINE_LOOP, 3, GLES20.GL_UNSIGNED_SHORT, i);
	        }
		}
		else {
			GLES20.glDrawElements(mode, size, dataType, 0);
		}
	}
	
}
