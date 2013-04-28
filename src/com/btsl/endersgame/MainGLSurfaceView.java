package com.btsl.endersgame;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class MainGLSurfaceView extends GLSurfaceView {

	public MainGLSurfaceView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		setDebugFlags(DEBUG_CHECK_GL_ERROR);
		setRenderer(new MainRenderer(context));
		setRenderMode(RENDERMODE_WHEN_DIRTY);
	}

}
