package com.btsl.endersgame;

import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;

public class MainRenderer implements Renderer {
	
	/* Need to remember context to get resources. */
	private final Context context;
	
	/* Transformation matrices */
	private float[] projection = new float[16];
	private float[] view = new float[16];
	private float[] model = new float[16];
	private float[] mvp = new float[16];
	
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
        
        // Set transformation. Using iVars to avoid allocation
//        Matrix.multiplyMM(mvp, 0, projection, 0, view, 0);
        Matrix.multiplyMM(mvp, 0, projection, 0, Camera.getView(), 0);
        program.setMVP(mvp);
        
        // Draw the test object
//        triangleComponent.Draw(program, GLES20.GL_TRIANGLES);
        
        // Draw the bunny
        bunnyMB.draw(program, GLES20.GL_TRIANGLES);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.perspectiveM(projection, 0, 45, ratio, .1f, 100f);
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		// Create our shader program
		program = new Program("default.vert", "default.frag", context);
		
		triangleComponent = new Component(Arrays.asList(TRIANGLE_VERTICES_DATA),
				Arrays.asList(TRIANGLE_NORMALS_DATA), Arrays.asList(TRIANGLE_ELEM_DATA));
		
		bunnyMB = OBJFile.createModelBufferFromFile("bunny.obj", context, "vertexCoordinates", null, null);
		
		// Set view properties
		Matrix.setLookAtM(
			view, 0,
			0.0f, 0.0f, -5.0f, 	 // Eye position
			0.0f, 0.0f, 0.0f,   // Eye target
			0.0f, 1.0f, 0.0f);   // Up vector
	}
	

    private final Float[] TRIANGLE_VERTICES_DATA = {
        -1.0f, -0.5f, 0.f,
        1.0f, -0.5f, 0.f,
        0.0f,  1.11803399f, 0.f 
    };
    
    private final Float[] TRIANGLE_NORMALS_DATA = {
    	0f, 0f, -1f,
    	0f, 0f, -1f,
    	0f, 0f, -1f,
    };
    
    private final Integer[] TRIANGLE_ELEM_DATA = { 0, 1, 2 };
    
    private Program program;
//    private ArrayBuffer<Float> triangleAB;
//    private ArrayBuffer<Float> triangleNormalAB;
//    private ElementArrayBuffer triangleEAB;
    private Component triangleComponent;
    private ModelBuffer bunnyMB;

}
