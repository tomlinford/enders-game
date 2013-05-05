package com.btsl.endersgame;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class BitmapTexture extends Texture 
{
	Bitmap bitmap;
	
	public BitmapTexture(Bitmap bitmap) {
		super(bitmap.getWidth(), bitmap.getHeight());
		this.bitmap = bitmap;
		bind();
	}
	
	@Override
	public void bind() {
		
		// Bind texture
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id);
		
		// Load the pixel data into the texture buffer
	    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
	    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
	    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
	    
	    // Unbind texture
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
	}
	
}
