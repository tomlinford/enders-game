package com.btsl;

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
	
	public MainRenderer(Context context) {
		this.context = context;
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        int err = GLES20.glGetError();
        program.Use();
        err = GLES20.glGetError();
        int vertexHandle = program.getAttribLocation("worldspacePosition");
        err = GLES20.glGetError();
        triangleAB.Use(vertexHandle);
        err = GLES20.glGetError();
        triangleEAB.draw(GLES20.GL_TRIANGLES);
        err = GLES20.glGetError();
        triangleAB.Unuse(vertexHandle);
        err = GLES20.glGetError();
        err = err + 1;
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(projection, 0, -ratio, ratio, -1, 1, 3, 7);
        int err = GLES20.glGetError();
        err = err + 1;
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		int err = GLES20.glGetError();
		program = new Program("default.vert", "default.frag", context);
        err = GLES20.glGetError();
		triangleAB = new ArrayBuffer(Arrays.asList(mTriangleVerticesData), 3);
        err = GLES20.glGetError();
		triangleEAB = new ElementArrayBuffer(Arrays.asList(TRIANGLE_ELEM_DATA));
        err = GLES20.glGetError();
		Matrix.setLookAtM(view, 0, 0, 0, -5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        err = GLES20.glGetError();
        err = err + 1;
	}
	

    private final Float[] mTriangleVerticesData = {
            -1.0f, -0.5f, 0.f,
            1.0f, -0.5f, 0.f,
            0.0f,  1.11803399f, 0.f };
    private final Integer[] TRIANGLE_ELEM_DATA = { 0, 1, 2 };
    
    private Program program;
    private ArrayBuffer triangleAB;
    private ElementArrayBuffer triangleEAB;

}
