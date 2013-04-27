package com.btsl.endersgame;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

public class Shader {
	
	private final int id;
	
	public Shader(String filename, Context context, int shaderType) {
		String source = "";
		try {
			InputStream is = context.getAssets().open(filename);
			source = new Scanner(is).useDelimiter("\\Z").next();
		} catch (IOException e) {
			e.printStackTrace();
		}

		id = GLES20.glCreateShader(shaderType);
		GLES20.glShaderSource(id, source);
		GLES20.glCompileShader(id);
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(id, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            Log.e("Shader", "Could not compile shader " + shaderType + ":");
            Log.e("Shader", GLES20.glGetShaderInfoLog(id));
            GLES20.glDeleteShader(id);
        }
	}
	
	public int getID() {
		return id;
	}

}
