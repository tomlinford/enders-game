package com.btsl.endersgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

public class Material {

	/** Specular component */
	public float Ns;
	
	/** Dissolve component */
	public float d;
	
	/** Ambient color */
	public float Kar, Kag, Kab;
	
	/** Diffuse color */
	public float Kdr, Kdg, Kdb;
	
	/** Specular color */
	public float Ksr, Ksg, Ksb;
	
	/** emissivity */
	public float Ker, Keg, Keb;
	
	public static MTLFile parseMTLFile(String filename, Context context) {
		try {
			return new MTLFile(new BufferedReader(new InputStreamReader(context.getAssets().open(filename))));
		} catch (IOException e) {
			Log.e("Material", "could not open the file " + filename);
		}
		return null;
	}
	
	
    public static class MTLFile {
        private HashMap<String, Material> map = new HashMap<String, Material>();
        
        public MTLFile(BufferedReader rd) throws IOException {
            String curMatName = "";
            Material m = null;

            for (String line = rd.readLine(); line != null; line = rd.readLine()) {
                if (line.length() < 3 || line.charAt(0) == '#') continue;
                String[] arr = line.split(" ");

                int start = 0;
                for (start = 0; start < arr.length && arr[start].length() == 0; start++);
                
                if (arr[start + 0].equals("newmtl")) {
                    curMatName = arr[start + 1];
                    m = new Material();
                } else if (arr[start + 0].equals("Ns")) {
                    m.Ns = Float.parseFloat(arr[start + 1]);
                } else if (arr[start + 0].equals("d")) {
                    m.d = Float.parseFloat(arr[start + 1]);
                } else if (arr[start + 0].equals("Ka")) {
                    m.Kar = Float.parseFloat(arr[start + 1]);
                    m.Kag = Float.parseFloat(arr[start + 2]);
                    m.Kab = Float.parseFloat(arr[start + 3]);
                } else if (arr[start + 0].equals("Kd")) {
                    m.Kdr = Float.parseFloat(arr[start + 1]);
                    m.Kdg = Float.parseFloat(arr[start + 2]);
                    m.Kdb = Float.parseFloat(arr[start + 3]);
                } else if (arr[start + 0].equals("Ks")) {
                    m.Ksr = Float.parseFloat(arr[start + 1]);
                    m.Ksg = Float.parseFloat(arr[start + 2]);
                    m.Ksb = Float.parseFloat(arr[start + 3]);
                } else if (arr[start + 0].equals("Ke")) { // last one in file
                    m.Ker = Float.parseFloat(arr[start + 1]);
                    m.Keg = Float.parseFloat(arr[start + 2]);
                    m.Keb = Float.parseFloat(arr[start + 3]);
                    map.put(curMatName, m);
                }
            }
        }
		
		/**
		 * Returns Material with given name
		 * @param mtlName
		 * @return
		 */
		public Material get(String mtlName) {
			return map.get(mtlName);
		}
		
		/**
		 * Returns 
		 * @return
		 */
		public Material first() {
			return map.values().iterator().next();
		}
	}
	
}
