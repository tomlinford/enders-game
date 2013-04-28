package com.btsl.endersgame;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

public class Program {
	
    // Program location on GPU
	private final int id;
    
    // Storing ID's for common matrices
	private final int mvpID;
    private final int mID;
    private final int vID;
    private final int pID;
	
	public Program(String vertexShaderFilename, String fragmentShaderFilename,
				   Context context) {
		this(new Shader(vertexShaderFilename, context, GLES20.GL_VERTEX_SHADER),
			new Shader(vertexShaderFilename, context, GLES20.GL_VERTEX_SHADER));
	}
	
	public Program(Shader vertexShader, Shader fragmentShader) {
		id = GLES20.glCreateProgram();
		attachShader(vertexShader);
		attachShader(fragmentShader);
        
        // Fetch location of common matrices
		mvpID = getUniformLocation("MVP");
        mID = getUniformLocation("M");
        vID = getUniformLocation("V");
        pID = getUniformLocation("P");
	}
	
    /* Attaches the given Shader object to our program */
	public void attachShader(Shader shader) {
		GLES20.glAttachShader(id, shader.getID());
		GLES20.glLinkProgram(id);

        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(id, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] != GLES20.GL_TRUE) {
            Log.e("Program", "Could not link program: ");
            Log.e("Program", GLES20.glGetProgramInfoLog(id));
            GLES20.glDeleteProgram(id);
        }
	}
    
    /* Creates a new shader of the given type from the given filename
     * and attaches it to our program. 'type' should be either
     * GLES20.GL_VERTEX_SHADER or GLES20.GL_FRAGMENT_SHADER.
     * */
    public void attachShader(String shaderFilename, int type, Context context) {
        Shader shader = new Shader(shaderFilename, context, type);
        attachShader(shader);
    }
	
    /* Call to use this shader program for subsequent rendering calls */
	public void use() {
		GLES20.glUseProgram(id);
	}
	
    /* Fetches the location for a given attribute in this shader program */
	public int getAttribLocation(String name) {
		int location = GLES20.glGetAttribLocation(id, name);
        if (location < 0)
            Log.e("Program", "Could not find attribute: " + name);
        return location;
	}
	
    /* Fetches the location for a given uniform in this shader program */
	public int getUniformLocation(String name) {
		int location = GLES20.glGetUniformLocation(id, name);
        if (location < 0)
            Log.e("Program", "Could not find uniform: " + name);
        return location;
	}
    
    /* Overloaded methods for assigning values to shader uniform variables */
    
    public void setUniform(String name, int value) {
        int location = getUniformLocation(name);
        if (location < 0)
            return;
        GLES20.glUniform1i(location, value);
    }
    
    public void setUniform(String name, float value) {
        int location = getUniformLocation(name);
        if (location < 0)
            return;
        GLES20.glUniform1f(location, value);
    }
    
    public void setUniform(String name, float x, float y) {
        int location = getUniformLocation(name);
        if (location < 0)
            return;
        GLES20.glUniform2f(location, x, y);
    }
    
    public void setUniform(String name, float x, float y, float z) {
        int location = getUniformLocation(name);
        if (location < 0)
            return;
        GLES20.glUniform3f(location, x, y, z);
    }
    
    public void setUniform(String name, float x, float y, float z, float w) {
        int location = getUniformLocation(name);
        if (location < 0)
            return;
        GLES20.glUniform4f(location, x, y, z, w);
    }
    
    public void setUniform(String name, Texture texture) {
    	int location = getUniformLocation(name);
        if (location < 0)
            return;
        
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture.getID());
        GLES20.glUniform1i(id, 0);
    }
    
    /* Methods for assigning values for transformation matrices
     * The location for these matrices on the GPU are stored to
     * avoid lookup every single time. 
     * */
	
	public void setMVP(float[] mvp) {
        if (mvpID > 0)
		    GLES20.glUniformMatrix4fv(mvpID, 1, false, mvp, 0);
	}

	public void setM(float[] m) {
        if (mID > 0)
		    GLES20.glUniformMatrix4fv(mID, 1, false, m, 0);
	}
    
	public void setV(float[] v) {
        if (vID > 0)
		    GLES20.glUniformMatrix4fv(vID, 1, false, v, 0);
	}
    
	public void setP(float[] p) {
        if (pID > 0)
		    GLES20.glUniformMatrix4fv(pID, 1, false, p, 0);
	}
	
}
