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
	
	/**
	 * Create Component with everything specified
	 * @param vertices
	 * @param normals
	 * @param tex
	 * @param indices
	 */
	public Component(List<Float> vertices, List<Float> normals, List<Float> tex, List<Integer> indices) {
		this(vertices, normals, indices);
		assert vertices.size() * 2 == tex.size() * 3
				: "Compenent: vertices and textures must have same number of vectors";
		this.tex = new ArrayBuffer<Float>(tex, 2);
		this.tex.setAttribute("texCoordinates");
	}
	
	/**
	 * Create Component from only vertices and normals
	 * @param vertices
	 * @param normals
	 * @param indices
	 */
	public Component(List<Float> vertices, List<Float> normals, List<Integer> indices) {
		this(vertices, indices);
		assert vertices.size() == normals.size()
				: "Compenent: vertices and normals must have same number of vectors";
		this.normals = new ArrayBuffer<Float>(normals, 3);
		this.normals.setAttribute("normalCoordinates");
	}
	
	/**
	 * Create Component from just vertices
	 * @param vertices
	 * @param indices
	 */
	public Component(List<Float> vertices, List<Integer> indices) {
		this.vertices = new ArrayBuffer<Float>(vertices, 3);
		this.vertices.setAttribute("vertexCoordinates");
		this.indices = new ElementArrayBuffer(indices);
	}
	
	/**
	 * Draws this Component. Mode specifies which type of primitive 
	 * to render, i.e. GLES20.GL_TRIANGLES or GLES20.GL_LINE_LOOP.
	 * @param program
	 * @param mode
	 */
	public void Draw(Program program, int mode) {
		
		// Load information for the drawElements call
		vertices.use(program);
		if (normals != null) normals.use(program);
		if (tex != null) tex.use(program);
		
		// Draw
		indices.draw(mode);
		
		// Unload information
		vertices.unuse(program);
		if (normals != null) normals.unuse(program);
		if (tex != null) tex.unuse(program);
	}
}
