package com.btsl.endersgame;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

public class Texture {
	
	int id;
	
	/* Creates a blank texture */
	public Texture() {
		id = genTexture();
	}
	
	/* Creates a new Texture from a file */
	public Texture(String filename, Context context)
	{
		id = genTexture();
	}
	
	/* Loads texture data into the GPU buffer */
	public void bind()
	{	
	    glBindTexture(GLES20.GL_TEXTURE_2D, id);
	    if (bitmap.data)
	    	GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, bitmap.width, bitmap.height, 0, GL_BGR, GL_UNSIGNED_BYTE, bitmap.data);
	    else
	    	GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
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
}
