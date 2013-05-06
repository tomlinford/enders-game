package com.btsl.endersgame;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainRenderer implements Renderer {
	
	/* Need to remember context to get resources. */
	private final Context context;
	
	/* Transformation matrices */
	private float[] model = new float[16];
	private float[] projection = new float[16];
	private float[] view = new float[16];
	private float[] viewProjection = new float[16];
	
	/* Animation path */
	public List<KeyFrame> keyFrames = Collections.synchronizedList(new ArrayList<KeyFrame>());
	
	public MainRenderer(Context context) {
		this.context = context;
		
		/*long now = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			KeyFrame frame = new KeyFrame();
			frame.position[0] = (i % 2) * -1 + 1;
			frame.position[1] = 0;
			frame.position[2] = 0;
			frame.time = now + (i - 1) * 1000;
			Log.e("Keyframe", "Keyframe at (" + frame.position[0] + ", " + frame.position[1] + ", " + frame.position[2] + ") for time" + frame.time);
			keyFrames.add(frame);
		}*/
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		
		long now = System.currentTimeMillis();
		ArrayList<KeyFrame> controlFrames = getControlFrames(now);
		Log.e("Animation", "Control frames found: " + controlFrames.size() + " for time: " + now);
		if (controlFrames.size() == 4) {
			CMSpline spline = new CMSpline(controlFrames.get(0).position, controlFrames.get(1).position, 
					controlFrames.get(2).position, controlFrames.get(3).position);
			float u = (float)(now - controlFrames.get(1).time) / (controlFrames.get(2).time - controlFrames.get(1).time);
			float[] position = spline.evaluate_vec(u);
			cubes[cubeIndex].setLocation(position[0], position[1], position[2]);
		}
		
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
        if (flat) flatSphere.draw(phongProgram, GLES20.GL_TRIANGLES, viewProjection, 0);
        else sphere.draw(phongProgram, GLES20.GL_TRIANGLES, viewProjection, 0);
//        texturedCube.draw(texturedPhongProgram, GLES20.GL_TRIANGLES, viewProjection, 0);
        shirt.draw(texturedPhongProgram, GLES20.GL_TRIANGLES, viewProjection, 0);
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
		
		shirt = OBJFile.createModelFromFile("shirt.obj", context, "vertexCoordinates",
				"texCoordinates", "normalCoordinates");
		shirt.translate(-3, 0, 0);
		
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
		
		flatSphere = OBJFile.createModelFromFile("sphere.obj", context, "vertexCoordinates", null, "normalCoordinates");
		flatSphere.translate(3, 0, 0);
		sphere = OBJFile.createModelFromFile("sphere.obj", context, "vertexCoordinates", null, "normalCoordinates", true);
		sphere.translate(3, 0, 0);
	}
	
	ArrayList<KeyFrame> getControlFrames(long time)
	{
	    ArrayList<KeyFrame> controlFrames = new ArrayList<KeyFrame>(4);
	    if (keyFrames.size() >= 4)
	    {
	        int i = 1;
	        while (i < keyFrames.size() && keyFrames.get(i).time < time)
	            i++;
	        
	        // Exclude edge cases where we don't have enough
	        // control points
	        if (i - 2 >= 0 &&
	            i + 1 < keyFrames.size())
	        {
	            controlFrames.add(keyFrames.get(i - 2));
	            controlFrames.add(keyFrames.get(i - 1));
	            controlFrames.add(keyFrames.get(i));
	            controlFrames.add(keyFrames.get(i + 1));
	        }
	    }
	    return controlFrames;
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
