package com.btsl.endersgame;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

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
        mTriangleVertices = ByteBuffer.allocateDirect(mTriangleVerticesData.length
                * FLOAT_SIZE_BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangleVertices.put(mTriangleVerticesData).position(0);
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(projection, 0, -ratio, ratio, -1, 1, 3, 7);
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		program = new Program("default.vert", "default.frag", context);
		
		Matrix.setLookAtM(view, 0, 0, 0, -5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
	}
	


    private static final int FLOAT_SIZE_BYTES = 4;
    private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 5 * FLOAT_SIZE_BYTES;
    private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
    private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 3;
    private final float[] mTriangleVerticesData = {
            // X, Y, Z, U, V
            -1.0f, -0.5f, 0, -0.5f, 0.0f,
            1.0f, -0.5f, 0, 1.5f, -0.0f,
            0.0f,  1.11803399f, 0, 0.5f,  1.61803399f };

    private FloatBuffer mTriangleVertices;
    private Program program;

}
