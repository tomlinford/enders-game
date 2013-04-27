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

	protected final int numElems;
	
	public ElementArrayBuffer(List<Integer> data) {
		super(data, GLES20.GL_ELEMENT_ARRAY_BUFFER);
		numElems = data.size();
	}
	
	public void draw(int mode) {
		bind();
		GLES20.glDrawElements(mode, numElems, dataType, 0);
	}

}
