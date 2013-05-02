/**
 * 
 */
package com.btsl.endersgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

/**
 * 
 * The OBJFile class represents a parsed object file. Having the ArrayLists associated
 * with the object file lets us modify them later on.
 * <br>
 * However, the most common usage will be with createModelBufferFromFile, which will
 * create the ModelBuffer given the file by using an instance of an OBJFile.
 * 
 * @author Tom
 *
 */
public class OBJFile {
	
	private ArrayList<Integer> indices = new ArrayList<Integer>();
	private ArrayList<Float> vertCoords = new ArrayList<Float>();
	private ArrayList<Float> texCoords = new ArrayList<Float>();
	private ArrayList<Float> normals = new ArrayList<Float>();
	
	/**
	 * Create a ModelBuffer from a file, also must signify what the attribute names
	 * will be
	 * @param filename
	 * @param context
	 * @param vAttrib vertex attribute name
	 * @param tAttrib texture attribute name
	 * @param nAttrib normal attribute name
	 * @return
	 */
	public static ModelBuffer createModelBufferFromFile(String filename, Context context,
			String vAttrib, String tAttrib, String nAttrib) {
		OBJFile objFile = new OBJFile(filename, context);
		ElementArrayBuffer elementBuffer = new ElementArrayBuffer(objFile.indices);
		ArrayBuffer<Float> vertexBuffer = new ArrayBuffer<Float>(objFile.vertCoords, 3);
		vertexBuffer.setAttribute(vAttrib);
		ArrayBuffer<Float> textureBuffer = null;
		if (!objFile.texCoords.isEmpty()) {
			textureBuffer = new ArrayBuffer<Float>(objFile.texCoords, 2);
			textureBuffer.setAttribute(tAttrib);
		}
		ArrayBuffer<Float> normalBuffer = null;
		if (!objFile.normals.isEmpty()) {
			normalBuffer = new ArrayBuffer<Float>(objFile.normals, 3);
			normalBuffer.setAttribute(nAttrib);
		}
		return new ModelBuffer(vertexBuffer, textureBuffer, normalBuffer, elementBuffer);
	}
	
	/**
	 * Generate OBJFile from given parameters
	 * @param filename
	 * @param context
	 */
	public OBJFile(String filename, Context context) {
		ArrayList<Float> vertCoords = new ArrayList<Float>();
		ArrayList<Float> texCoords = new ArrayList<Float>();
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<VertexIndex> vertexIndices = new ArrayList<VertexIndex>();
		
		// read in everything from the file and store it in local ArrayLists
		try {
			BufferedReader br = new BufferedReader(new
					InputStreamReader(context.getAssets().open(filename)));
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				if (line.length() < 3 || line.charAt(0) == '#') continue;
				String[] arr = line.split(" ");
				if (arr[0].equals("v")) {
					addFloats(arr, vertCoords);
				} else if (arr[0].equals("vt")) {
					addFloats(arr, texCoords);
				} else if (arr[0].equals("vn")) {
					addFloats(arr, normals);
				} else if (arr[0].equals("f")) {
					addFaces(arr, vertexIndices);
				}
				// TODO: other parsing facilities should be added here (such as materials)
			}
		} catch (IOException e) {
			Log.e("OBJFile", "Could not open the file: " + filename);
		}

		// find the maximum float value, so we can normalize it
		float max = 0f;
		for (float v : vertCoords) if (max < v) max = v;
		
		// keep track of used indices to allow reuse
		HashMap<VertexIndex, Integer> indexMap = new HashMap<VertexIndex, Integer>();
		
		for (VertexIndex vi : vertexIndices) {
			if (indexMap.containsKey(vi)) {
				indices.add(indexMap.get(vi));
			} else {
				indexMap.put(vi, this.vertCoords.size() / 3);
				indices.add(this.vertCoords.size() / 3);
				for (int i = 0; i < 3; i++)
					this.vertCoords.add(vertCoords.get(vi.v * 3 + i) / max);
				if (!texCoords.isEmpty())
					for (int i = 0; i < 2; i++)
						this.texCoords.add(texCoords.get(vi.t * 2 + i));
				if (!normals.isEmpty())
					for (int i = 0; i < 3; i++)
						this.normals.add(normals.get(vi.n * 3 + i));
			}
		}
	}
	
	/**
	 * Add the floats from arr to al starting at arr[1]
	 * @param arr
	 * @param al
	 */
	private static void addFloats(String[] arr, ArrayList<Float> al) {
		for (int i = 1; i < arr.length; i++)
			al.add(Float.parseFloat(arr[i]));
	}
	
	/**
	 * adds the VertexIndex from arr into starting at arr[1]
	 * @param arr
	 * @param al
	 */
	private static void addFaces(String[] arr, ArrayList<VertexIndex> al) {
		for (int i = 1; i < arr.length; i++) {
			String[] faceArr = arr[i].split("/");
			VertexIndex vi = new VertexIndex();
			vi.v = Integer.parseInt(faceArr[0]) - 1;
			if (faceArr.length > 1 && !faceArr[1].isEmpty())
				vi.t = Integer.parseInt(faceArr[1]) - 1;
			if (faceArr.length > 2 && !faceArr[2].isEmpty())
				vi.n = Integer.parseInt(faceArr[2]) - 1;
			al.add(vi);
		}
	}
	
	/**
	 * Contains the three indices related to the v, t, and n.
	 * 
	 * @author Tom
	 *
	 */
	private static class VertexIndex {
		public int v, t, n;

		/**
		 * Eclipse-generated, lets us use a HashMap
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + n;
			result = prime * result + t;
			result = prime * result + v;
			return result;
		}

		/**
		 * Eclipse-generated, lets us use a HashMap
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			VertexIndex other = (VertexIndex) obj;
			if (n != other.n)
				return false;
			if (t != other.t)
				return false;
			if (v != other.v)
				return false;
			return true;
		}
	}

}
