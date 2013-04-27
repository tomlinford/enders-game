package com.btsl.endersgame;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

public class Texture {
	
	int id, format;
	int width, height;
	
	/* Note: this is INPUT pixel data, not
	 * the "live" pixel data on the GPU that
	 * may have changed
	 */
	Buffer pixels;
	
	/* Creates a blank texture */
	public Texture(int width, int height) {
		this(width, height, GLES20.GL_RGBA, null);
	}
	
	/* Creates a texture of the given width and height
	 * from the given pixel data. 'format' specifies the
	 * format of the data in 'pixels'. This should be
	 * GLES20.RGBA or GLES20.RGBA.
	 * */
	public Texture(int width, int height, int format, Buffer pixels) {
		id = genTexture();
		this.width = width;
		this.height = height;
		this.format = format;
		this.pixels = pixels;
	}
	
	/* Creates a new Texture from a file */
	public Texture(String filename, Context context)
	{
		id = genTexture();
	}
	
	/* Loads texture data into the GPU buffer */
	public void bind()
	{	
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id);
		
		// Determine the pixel data type
		int type;
		if (pixels instanceof FloatBuffer) {
			type = GLES20.GL_FLOAT;
		}
		else if (pixels instanceof IntBuffer) {
			type = GLES20.GL_UNSIGNED_INT;
		}
		else if (pixels instanceof ByteBuffer) {
			type = GLES20.GL_UNSIGNED_BYTE;
		}
		else {
			Log.e("Texture", "Attempted to bind a texture of an unsupported type");
			return;
		}
		
		// Load the pixel data into the texture buffer
	    GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, format, type, pixels);
	    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
	    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
	}
	
	/* Generates a texture buffer */
	private static int genTexture()
	{
		int[] buf = new int[1];
		GLES20.glGenTextures(1, buf, 0);
		return buf[0];
	}
	
	/* Returns this texture's ID */
	public int getID() {
		return id;
	}
	
}
