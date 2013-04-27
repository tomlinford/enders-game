package com.btsl.endersgame;

public abstract class AbstractBuffer {
	
	protected final int id;
	protected final int target;
	
	public AbstractBuffer(int id, int target) {
		this.id = id;
		this.target = target;
	}
	
	abstract public void bind();

}
