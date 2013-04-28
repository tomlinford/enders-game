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
	
	public OBJFile(String filename, Context context) {
		ArrayList<Float> vertCoords = new ArrayList<Float>();
		ArrayList<Float> texCoords = new ArrayList<Float>();
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<Integer> vertexIndices = new ArrayList<Integer>();
		float max = 0f;
		
		try {
			BufferedReader br = new BufferedReader(new
					InputStreamReader(context.getAssets().open(filename)));
			for (String line = br.readLine(); !line.isEmpty(); line = br.readLine()) {
				String[] arr = line.split(" ");
				if (arr[0].equals("v")) {
					
				}
			}
		} catch (IOException e) {
			Log.e("OBJFile", "Could not open the file: " + filename);
		}
	}

}
