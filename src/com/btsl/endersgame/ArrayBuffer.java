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
public class ArrayBuffer<T> extends DataBuffer<T> {

	private String attribute = null;
	private int location;
	private final int vertexSize;
	
	public ArrayBuffer(List<T> data, int vertexSize) {
		super(data, GLES20.GL_ARRAY_BUFFER);
		this.vertexSize = vertexSize;
        this.location = -1;
	}
	
	public void setAttribute(String attribute) {
		this.attribute = attribute;
        this.location = -1;
	}
	
	public void use(Program program) {
		if (attribute == null) {
			Log.e("Buffer", "Attempted to bind buffer not yet associated with an attribute");
            return;
        }
		
		if (location < 0) {
            location = program.getAttribLocation(attribute);
            if (location < 0) {
    			Log.e("Buffer", "Attempted to bind buffer to an attribute not found in the current shader program");
                return;
            }
        }
		
		GLES20.glEnableVertexAttribArray(location);
		bind();
		GLES20.glVertexAttribPointer(location, vertexSize, dataType, false, 0, 0);
	}
	
	public void unuse(Program program) {
		GLES20.glDisableVertexAttribArray(location);
	}
	
}
