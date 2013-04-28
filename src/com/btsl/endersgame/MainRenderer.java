package com.btsl.endersgame;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

public class MainRenderer implements Renderer {
	
	/** Need to remember context to get resources. */
	private final Context context;
	
	private float[] projection = new float[16];
	private float[] view = new float[16];
	private float[] mvp = new float[16];
	
	public MainRenderer(Context context) {
		this.context = context;
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        program.Use();
        Matrix.multiplyMM(mvp, 0, projection, 0, view, 0);
        program.setMVP(mvp);
        int vertexHandle = program.getAttribLocation("worldspacePosition");
        triangleAB.Use(vertexHandle);
        triangleEAB.draw(GLES20.GL_TRIANGLES);
        triangleAB.Unuse(vertexHandle);
      }

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.perspectiveM(projection, 0, 45f, ratio, .1f, 100f);
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		program = new Program("default.vert", "default.frag", context);
		triangleAB = new ArrayBuffer(Arrays.asList(mTriangleVerticesData), 3);
		triangleEAB = new ElementArrayBuffer(Arrays.asList(TRIANGLE_ELEM_DATA));
		Matrix.setLookAtM(view, 0, 0, 0, -5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
	}
	

    private final Float[] mTriangleVerticesData = {
            -1.0f, -0.5f, 0.f,
            1.0f, -0.5f, 0.f,
            0.0f,  1.11803399f, 0.f };
    private final Short[] TRIANGLE_ELEM_DATA = { 0, 1, 2 };
    
    private Program program;
    private ArrayBuffer triangleAB;
    private ElementArrayBuffer triangleEAB;

}
