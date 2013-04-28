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
		GLES20.glDrawElements(mode, size, dataType, 0);
	}
	
}
