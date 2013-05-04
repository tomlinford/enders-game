package com.btsl.endersgame;

import android.opengl.GLES20;

public abstract class AbstractBuffer {
	
	protected final int id;
	protected final int target;
	protected final int size;
	
	public AbstractBuffer(int id, int target, int size) {
		this.id = id;
		this.target = target;
		this.size = size;
	}
	
	/**
	 * @return this buffer's ID
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * @return the size of this buffer.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Binds the buffer
	 */
	public void bind() {
		GLES20.glBindBuffer(target, id);
	}
	
	/**
	 * Unbinds the buffer
	 */
	public void unbind() {
		GLES20.glBindBuffer(target, 0);
	}
}
