package com.btsl.endersgame;

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
	private float[] viewProjection = new float[16];
	
	public MainRenderer(Context context) {
		this.context = context;
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		
		// Clear frame
		GLES20.glClearColor(0.1f, 0.1f, .1f, 1.0f);
		GLES20.glClearDepthf(1.0f);
        GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        
        // Use our custom shader
        phongProgram.use();
        phongProgram.setUniform("worldspaceCameraPosition", Camera.getPosition()[0],
        		Camera.getPosition()[1], Camera.getPosition()[2]);
        texturedPhongProgram.use();
        texturedPhongProgram.setUniform("worldspaceCameraPosition", Camera.getPosition()[0],
        		Camera.getPosition()[1], Camera.getPosition()[2]);
        
        Matrix.multiplyMM(viewProjection, 0, projection, 0, Camera.getView(), 0);
        
        // bunny.draw(program, GLES20.GL_TRIANGLES, viewProjection, 0);
        cubes[cubeIndex].draw(program, GLES20.GL_LINE_LOOP, viewProjection, 0);
//        if (flat) flatSphere.draw(phongProgram, GLES20.GL_TRIANGLES, viewProjection, 0);
//        else sphere.draw(phongProgram, GLES20.GL_TRIANGLES, viewProjection, 0);
//        texturedCube.draw(texturedPhongProgram, GLES20.GL_TRIANGLES, viewProjection, 0);
//        shirt.draw(texturedPhongProgram, GLES20.GL_TRIANGLES, viewProjection, 0);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.perspectiveM(projection, 0, 45, ratio, .1f, 100f);
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		GLES20.glEnable( GLES20.GL_DEPTH_TEST );
		GLES20.glDepthFunc( GLES20.GL_LEQUAL );
		GLES20.glDepthMask( true );
		// Create our shader program
		program = new Program("default.vert", "default.frag", context);
		phongProgram = new Program("phong_vert.glsl", "phong_frag.glsl", context);
		texturedPhongProgram = new Program("phong_vert.glsl", "textured_phong_frag.glsl", context);
		
//		shirt = OBJFile.createModelFromFile("shirt.obj", context, "vertexCoordinates",
//				"texCoordinates", "normalCoordinates");
		
		//bunny = OBJFile.createModelFromFile("bunny.obj", context, "vertexCoordinates", null, null);
		OBJFile cubeOBJ = new OBJFile("cube.obj", context);
		cubes[0] = cubeOBJ.genModel("vertexCoordinates", "texCoordinates", "normalCoordinates");
		for (int i = 1; i < cubes.length; i++) {
			Subdivider.Subdivide(cubeOBJ);
			cubes[i] = cubeOBJ.genModel("vertexCoordinates", "texCoordinates", "normalCoordinates");
		}
//		texturedCube = OBJFile.createModelFromFile("textured_cube.obj", context, "vertexCoordinates",
//				"texCoordinates", "normalCoordinates");
//		texturedCube.translate(-3, 0, 0);
//		
//		flatSphere = OBJFile.createModelFromFile("sphere.obj", context, "vertexCoordinates", null, "normalCoordinates");
//		flatSphere.translate(3, 0, 0);
//		sphere = OBJFile.createModelFromFile("sphere.obj", context, "vertexCoordinates", null, "normalCoordinates", true);
//		sphere.translate(3, 0, 0);
	}
	
	public void subdivideCube() {
		if (cubeIndex < cubes.length - 1) cubeIndex++;
	}
	
	public void swapFlat() {
		flat = !flat;
	}

    private Program program;
    private Program phongProgram;
    private Program texturedPhongProgram;
    private Model shirt;
//    private Model bunny;
    private Model sphere;
    private Model flatSphere;
    private boolean flat;
    private Model[] cubes = new Model[6];
    private Model texturedCube;
    private int cubeIndex;

}
