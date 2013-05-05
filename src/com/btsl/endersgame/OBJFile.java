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

	public ArrayList<Integer> indices = new ArrayList<Integer>();
	public ArrayList<Float> vertCoords = new ArrayList<Float>();
	public ArrayList<Float> texCoords = new ArrayList<Float>();
	public ArrayList<Float> normals = new ArrayList<Float>();
	private Material mat;
	
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
	public static Model createModelFromFile(String filename, Context context,
			String vAttrib, String tAttrib, String nAttrib) {
		OBJFile objFile = new OBJFile(filename, context);
		return objFile.genModel(vAttrib, tAttrib, nAttrib);
	}
	
	/**
	 * Create a ModelBuffer from a file, also must signify what the attribute names
	 * will be
	 * @param filename
	 * @param context
	 * @param vAttrib
	 * @param tAttrib
	 * @param nAttrib
	 * @param averageNormals averageNormals set to be true to average the normals for phong shading
	 * 	as opposed to flat shading
	 * @return
	 */
	public static Model createModelFromFile(String filename, Context context,
			String vAttrib, String tAttrib, String nAttrib, boolean averageNormals) {
		OBJFile objFile = new OBJFile(filename, context, averageNormals);
		return objFile.genModel(vAttrib, tAttrib, nAttrib);
	}
	
	/**
	 * Generate OBJFile from given parameters
	 * @param filename
	 * @param context
	 */
	public OBJFile(String filename, Context context) {
		this(filename, context, false);
	}
	
	/**
	 * Generate OBJFile from given parameters
	 * @param filename
	 * @param context
	 * @param averageNormals set to be true to average the normals for phong shading
	 * 	as opposed to flat shading
	 */
	public OBJFile(String filename, Context context, boolean averageNormals) {
		ArrayList<Float> vertCoords = new ArrayList<Float>();
		ArrayList<Float> texCoords = new ArrayList<Float>();
		ArrayList<Float> normalCoords = new ArrayList<Float>();
		ArrayList<VertexIndex> vertexIndices = new ArrayList<VertexIndex>();
		Material.MTLFile mtlFile = null;
		
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
					addFloats(arr, normalCoords);
				} else if (arr[0].equals("f")) {
					addFaces(arr, vertexIndices);
				} else if (arr[0].equals("mtllib")) {
					mtlFile = Material.parseMTLFile(arr[1], context);
				}
			}
		} catch (IOException e) {
			Log.e("OBJFile", "Could not open the file: " + filename);
		}

		// find the maximum float value, so we can normalize it
		float max = 0f;
		for (float v : vertCoords) if (max < v) max = v;
		
		// average the normals if necessary
		if (averageNormals) {
			HashMap<Integer, ArrayList<Integer>> normalIndices = new HashMap<Integer, ArrayList<Integer>>();
			for (VertexIndex vi : vertexIndices) {
				if (!normalIndices.containsKey(vi.v)) normalIndices.put(vi.v, new ArrayList<Integer>());
				normalIndices.get(vi.v).add(vi.n);
			}
			for (Integer indexKey : normalIndices.keySet()) {
				ArrayList<Integer> indices = normalIndices.get(indexKey);
				float ax = 0, ay = 0, az = 0;
				for (int index : indices) {
					ax += normalCoords.get(index * 3);
					ay += normalCoords.get(index * 3 + 1);
					az += normalCoords.get(index * 3 + 2);
				}
				ax /= indices.size();
				ay /= indices.size();
				az /= indices.size();
				for (VertexIndex vi : vertexIndices) {
					if (vi.v == indexKey) vi.n = normalCoords.size() / 3;
				}
				normalCoords.add(ax);
				normalCoords.add(ay);
				normalCoords.add(az);
			}
		}
		
		// keep track of used indices to allow reuse
		HashMap<VertexIndex, Integer> indexMap = new HashMap<VertexIndex, Integer>();
		
		int count = 0;
		for (VertexIndex vi : vertexIndices) {
			Integer index = indexMap.get(vi);
			if (index == null) {
				index = count++;
				indexMap.put(vi, index);
				for (int i = 0; i < 3; i++)
					this.vertCoords.add(vertCoords.get(vi.v * 3 + i) / max);
				if (!texCoords.isEmpty())
					for (int i = 0; i < 2; i++)
						this.texCoords.add(texCoords.get(vi.t * 2 + i));
				if (!normalCoords.isEmpty())
					for (int i = 0; i < 3; i++)
						this.normals.add(normalCoords.get(vi.n * 3 + i));
			}
			indices.add(index);
		}
		if (mtlFile != null) mat = mtlFile.first();
	}
	
	/**
	 * Generate a model from instance of OBJFile
	 * @param vAttrib
	 * @param tAttrib
	 * @param nAttrib
	 * @return
	 */
	public Model genModel(String vAttrib, String tAttrib, String nAttrib) {
		ElementArrayBuffer elementBuffer = new ElementArrayBuffer(indices);
		ArrayBuffer<Float> vertexBuffer = new ArrayBuffer<Float>(vertCoords, 3);
		vertexBuffer.setAttribute(vAttrib);
		ArrayBuffer<Float> textureBuffer = null;
		if (!texCoords.isEmpty()) {
			textureBuffer = new ArrayBuffer<Float>(texCoords, 2);
			textureBuffer.setAttribute(tAttrib);
		}
		ArrayBuffer<Float> normalBuffer = null;
		if (!normals.isEmpty()) {
			normalBuffer = new ArrayBuffer<Float>(normals, 3);
			normalBuffer.setAttribute(nAttrib);
		}
		return new Model(new ModelBuffer(vertexBuffer, textureBuffer, normalBuffer, elementBuffer), mat);
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
	 * adds the VertexIndex from arr into al starting at arr[1]
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
