package com.btsl.endersgame;

import java.util.List;

public class Component {
	
	// Component geometry data
	ArrayBuffer<Float> vertices;
	ArrayBuffer<Float> normals;
	ArrayBuffer<Float> tex;
	ElementArrayBuffer indices;
	
	// Component-specific orientation data
	float[] orientation;
	
	public Component(List<Float> vertices, List<Float> normals, List<Float> tex, List<Integer> indices) {
		this.vertices = new ArrayBuffer<Float>(vertices);
		this.normals = new ArrayBuffer<Float>(normals);
		this.tex = new ArrayBuffer<Float>(tex);
		this.indices = new ElementArrayBuffer(indices);
	}
	
	/* Mode specifies which type of primitive to render, 
	 * i.e. GLES20.GL_TRIANGLES or GLES20.GL_LINE_LOOP
	 * */
	public void Draw(Program program, int mode) {
		
		// Load information for the drawElements call
		vertices.use(program);
		normals.use(program);
		tex.use(program);
		
		// Draw
		indices.draw(mode);
		
		// Unload information
		vertices.unuse(program);
		normals.unuse(program);
		tex.unuse(program);
	}
}
