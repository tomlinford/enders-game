package com.btsl.endersgame;

/**
 * A ModelBuffer contains all of the buffers related to a model, a vertex, texture,
 * and normal ArrayBuffer, as well as an ElementArrayBuffer to store the indices.
 * <br>
 * Note that the texture and normal ArrayBuffers may be null, and if they are null,
 * then they won't be used in the draw method
 * 
 * @author Tom
 *
 */
public class ModelBuffer {
	
	private ArrayBuffer<Float> vertexBuffer;
	private ArrayBuffer<Float> textureBuffer;
	private ArrayBuffer<Float> normalBuffer;
	
	private ElementArrayBuffer elementBuffer;

	/**
	 * Basic constructor
	 * @param vertexBuffer
	 * @param textureBuffer
	 * @param normalBuffer
	 * @param elementBuffer
	 */
	public ModelBuffer(ArrayBuffer<Float> vertexBuffer,
			ArrayBuffer<Float> textureBuffer, ArrayBuffer<Float> normalBuffer,
			ElementArrayBuffer elementBuffer) {
		this.vertexBuffer = vertexBuffer;
		this.textureBuffer = textureBuffer;
		this.normalBuffer = normalBuffer;
		this.elementBuffer = elementBuffer;
	}
	
	/**
	 * Draw the ModelBuffer with the specified program and mode
	 * @param p
	 * @param mode
	 */
	public void draw(Program p, int mode) {
		vertexBuffer.use(p);
		if (textureBuffer != null)
			textureBuffer.use(p);
		if (normalBuffer != null)
			normalBuffer.use(p);
		
		elementBuffer.draw(mode);

		vertexBuffer.unuse(p);
		if (textureBuffer != null)
			textureBuffer.unuse(p);
		if (normalBuffer != null)
			normalBuffer.unuse(p);
	}
	

}
