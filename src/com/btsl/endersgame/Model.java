package com.btsl.endersgame;

import java.util.List;

import android.opengl.Matrix;

public class Model {
	
	/** ModelBuffer for OpenGL */
	ModelBuffer modelBuf;
	
	/** Model matrix */
	float[] model = new float[16];
	
	/** Material, can be null */
	private Material mat;
	
	/** Static matrix for holding temporary mvp value */
	static float[] mvp = new float[16];
	
	/**
	 * Create Component with everything specified.
	 * <br>
	 * If no normals are desired, simply pass in null for the normals argument
	 * @param vertices
	 * @param normals
	 * @param tex
	 * @param indices
	 */
	public Model(List<Float> vertices, List<Float> normals, List<Float> tex, List<Integer> indices) {
		this(genModelBuf(vertices, normals, tex, indices), null);
	}
	
	/**
	 * Create Component from only vertices and normals
	 * @param vertices
	 * @param normals
	 * @param indices
	 */
	public Model(List<Float> vertices, List<Float> normals, List<Integer> indices) {
		this(vertices, normals, null, indices);
	}
	
	/**
	 * Create Component from just vertices
	 * @param vertices
	 * @param indices
	 */
	public Model(List<Float> vertices, List<Integer> indices) {
		this(vertices, null, null, indices);
	}
	
	/**
	 * Create Model from a ModelBuffer and a Material. Used by OBJFile
	 * @param modelBuf
	 * @param mat
	 */
	public Model(ModelBuffer modelBuf, Material mat) {
		this.modelBuf = modelBuf;
		this.mat = mat;
		Matrix.setIdentityM(model, 0);
	}
	
	/* Mode specifies which type of primitive to render, 
	 * i.e. GLES20.GL_TRIANGLES or GLES20.GL_LINE_LOOP
	 * */
	public void draw(Program program, int mode, float[] viewProjection, int offset) {
		Matrix.multiplyMM(mvp, 0, viewProjection, offset, model, 0);
		program.setMVP(mvp);
		program.setM(model);
		program.setUniform(mat);
		modelBuf.draw(program, mode);
	}
	
	
	/**
	 * Generate ModelBuffer from various data (can be passed in as null)
	 * @param vertices
	 * @param normals
	 * @param tex
	 * @param indices
	 * @return
	 */
	static private ModelBuffer genModelBuf(List<Float> vertices, List<Float> normals,
			List<Float> tex, List<Integer> indices) {
		ArrayBuffer<Float> vertexBuf = new ArrayBuffer<Float>(vertices, 3);
		vertexBuf.setAttribute("vertexCoordinates");
		ArrayBuffer<Float> textureBuf = null;
		if (tex != null) {
			textureBuf = new ArrayBuffer<Float>(tex, 2);
			textureBuf.setAttribute("texCoordinates");
		}
		ArrayBuffer<Float> normalBuf = null;
		if (normals != null) {
			normalBuf = new ArrayBuffer<Float>(normals, 3);
			normalBuf.setAttribute("normalCoordinates");
		}
		ElementArrayBuffer indexBuf = new ElementArrayBuffer(indices);
		return new ModelBuffer(vertexBuf, textureBuf, normalBuf, indexBuf);
	}
}
