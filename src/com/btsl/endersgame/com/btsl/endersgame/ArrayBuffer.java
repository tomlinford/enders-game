/**
 * 
 */
package com.btsl.endersgame;

import java.util.List;

import android.opengl.GLES20;

/**
 * @author Tom
 *
 * Currently only implemented for floats
 */
public class ArrayBuffer extends DataBuffer<Float> {
	
	protected final int vertexSize;

	/**
	 * 
	 * @param data The data to put into the OpenGL buffer
	 * @param vertexSize ie, this should be 3 if this buffer refers to a vec3
	 */
	public ArrayBuffer(List<Float> data, int vertexSize) {
		super(data, GLES20.GL_ARRAY_BUFFER);
		this.vertexSize = vertexSize;
	}
	
	public void Use(int attribHandle) {
		GLES20.glEnableVertexAttribArray(attribHandle);
		bind();
		GLES20.glVertexAttribPointer(attribHandle, vertexSize,
									dataType, false, 0, 0);
	}
	
	public void Unuse(int attribHandle) {
		GLES20.glDisableVertexAttribArray(attribHandle);
	}
}
