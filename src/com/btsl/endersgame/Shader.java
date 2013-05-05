package com.btsl.endersgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

public class Shader {
	
	private final int id;
	
	/* Creates a shader of the specified type from the specified
	 * file. The type should be either GLES20.GL_VERTEX_SHADER or
	 * GLES20.GL_FRAGMENT_SHADER.
	 */
	public Shader(String filename, Context context, int shaderType) {
        
        // Read program source into a string
		String source = "";
		try {
			StringBuilder sb = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));
			for (String line = rd.readLine(); line != null; line = rd.readLine()) sb.append(line + "\n");
			source = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}

        // Create and compile shader from the source string
		id = GLES20.glCreateShader(shaderType);
		GLES20.glShaderSource(id, source);
		GLES20.glCompileShader(id);
        
        // Error handling
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(id, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            Log.e("Shader", "Could not compile shader " + shaderType + ":");
            Log.e("Shader", GLES20.glGetShaderInfoLog(id));
            GLES20.glDeleteShader(id);
        }
	}
	
	/* Returns this shader's ID */
	public int getID() {
		return id;
	}

}
