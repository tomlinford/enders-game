package com.btsl.endersgame;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

public class Program {
	
	private final int id;
	private final int mvpID;
	
	public Program(String vertexShaderFilename, String fragmentShaderFilename,
					Context context) {
		this(new Shader(vertexShaderFilename, context, GLES20.GL_VERTEX_SHADER),
			new Shader(vertexShaderFilename, context, GLES20.GL_VERTEX_SHADER));
	}
	
	public Program(Shader vertexShader, Shader fragmentShader) {
		id = GLES20.glCreateProgram();
		attachShader(vertexShader);
		attachShader(fragmentShader);
		mvpID = getUniformLocation("MVP");
	}
	
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
	
	public void Use() {
		GLES20.glUseProgram(id);
	}
	
	public int getAttribLocation(String name) {
		return GLES20.glGetAttribLocation(id, name);
	}
	
	public int getUniformLocation(String name) {
		return GLES20.glGetUniformLocation(id, name);
	}
	
	public void setMVP(float[] mvp) {
		GLES20.glUniformMatrix4fv(mvpID, 1, false, mvp, 0);
	}

}
