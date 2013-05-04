/**
 * 
 */
package com.btsl.endersgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

/**
 * @author Tom
 *
 */
public class OBJFile {
	
	// OBJ file delimiters
	public static final String VERTEX = "v";
	public static final String TEX = "t";
	public static final String NORMAL = "vn";
	public static final String FACE = "f";
	public static final String GROUP = "g";
	public static final String MAT = "usemtl";
	
	// Material file delimiters
	public static final String MTL = "mtllib";
	public static final String AMBIENT_COLOR = "Ka";
	public static final String DIFFUSE_COLOR = "Kd";
	public static final String SPECULAR_COLOR = "Ks";
	public static final String ILLUM = "illum";
	public static final String SHININESS = "Ns";
	public static final String AMBIENT_MAP = "map_Ka";
	public static final String DIFFUSE_MAP = "map_Kd";
	public static final String BUMP_MAP = "map_bump";
	
	public OBJFile(String filename, Context context) {
		ArrayList<Float> vertexCoords = new ArrayList<Float>();
		ArrayList<Float> texCoords = new ArrayList<Float>();
		ArrayList<Float> normalCoords = new ArrayList<Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		
		parseOBJFile(filename, context, vertexCoords, texCoords, normalCoords, indices);
	}
	
	private static void parseOBJFile(String filename, Context context,
						   ArrayList<Float> vertexCoords, 
						   ArrayList<Float> texCoords, 
						   ArrayList<Float> normalCoords, 
						   ArrayList<Integer> indices) 
	{
		float max = 0.0f;
		
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(context.getAssets().open(filename)));
			for (String line = br.readLine(); !line.isEmpty(); line = br.readLine()) {
				String[] arr = line.split(" ");
				
				if (arr[0].equals(VERTEX)) {
					
				}
				else if (arr[0].equals(TEX)) {
					
				}
				else if (arr[0].equals(NORMAL)) {
					
				}
				else if (arr[0].equals(FACE)) {
					
				}
			}
		} catch (IOException e) {
			Log.e("OBJFile", "Could not open the file: " + filename);
		}
	}
	
	private static void parseMaterialFile(String filename) {
		
	}

}
