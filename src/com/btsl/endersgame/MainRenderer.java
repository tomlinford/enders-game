package com.btsl.endersgame;

import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

public class MainRenderer implements Renderer {
	
	/* Need to remember context to get resources. */
	private final Context context;
	
	/* Transformation matrices */
	private float[] projection = new float[16];
	private float[] view = new float[16];
	
	public MainRenderer(Context context) {
		this.context = context;
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		
		// Clear frame
		GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        
        // Use our custom shader
        program.use();
        
        // Set transformation
        float[] model = new float[16];
        float[] MV = new float[16];
        float[] MVP = new float[16];
        Matrix.setIdentityM(model, 0);
        Matrix.multiplyMM(MV, 0, view, 0, model, 0);
        Matrix.multiplyMM(MVP, 0, projection, 0, MV, 0);
        program.setMVP(MVP);
        
        // Draw the test object
        triangleAB.use(program);
        triangleEAB.draw(GLES20.GL_TRIANGLES);
        triangleAB.unuse(program);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(projection, 0, -ratio, ratio, -1, 1, 3, 7);
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		// Create our shader program
		program = new Program("default.vert", "default.frag", context);
		
		// Test object
		triangleAB.setAttribute("worldspacePosition");
		
		// Set view properties
		Matrix.setLookAtM(
			view, 0,
			0.0f, 0.0f, 0.0f, 	 // Eye position
			-5.0f, 0.0f, 0.0f,   // Eye target
			0.0f, 1.0f, 0.0f);   // Up vector
	}
	

    private final Float[] TRIANGLE_VERTICES_DATA = {
        -1.0f, -0.5f, 0.f,
        1.0f, -0.5f, 0.f,
        0.0f,  1.11803399f, 0.f 
    };
    
    private final Integer[] TRIANGLE_ELEM_DATA = { 0, 1, 2 };
    
    private Program program;
    private ArrayBuffer<Float> triangleAB = new ArrayBuffer<Float>(Arrays.asList(TRIANGLE_VERTICES_DATA));
    private ElementArrayBuffer triangleEAB = new ElementArrayBuffer(Arrays.asList(TRIANGLE_ELEM_DATA));

}
