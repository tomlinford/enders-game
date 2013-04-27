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
public class ArrayBuffer<T> extends DataBuffer<T> {

	public ArrayBuffer(List<T> data) {
		super(data, GLES20.GL_ARRAY_BUFFER);
	}
	
}
