package com.btsl.endersgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

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
	
	
	public static class MTLFile {
		private HashMap<String, Material> map = new HashMap<String, Material>();
		
		public MTLFile(BufferedReader rd) throws IOException {
			String curMatName = "";
			Material m = null;

			for (String line = rd.readLine(); line != null; line = rd.readLine()) {
				if (line.length() < 3 || line.charAt(0) == '#') continue;
				String[] arr = line.split(" ");
				
				if (arr[0].equals("newmtl")) {
					curMatName = arr[1];
					m = new Material();
				} else if (arr[0].equals("Ns")) {
					m.Ns = Float.parseFloat(arr[1]);
				} else if (arr[0].equals("d")) {
					m.d = Float.parseFloat(arr[1]);
                } else if (arr[0].equals("Ka")) {
                    m.Kar = Float.parseFloat(arr[1]);
                    m.Kag = Float.parseFloat(arr[2]);
                    m.Kab = Float.parseFloat(arr[3]);
                } else if (arr[0].equals("Kd")) {
                    m.Kdr = Float.parseFloat(arr[1]);
                    m.Kdg = Float.parseFloat(arr[2]);
                    m.Kdb = Float.parseFloat(arr[3]);
                } else if (arr[0].equals("Ks")) {
                    m.Ksr = Float.parseFloat(arr[1]);
                    m.Ksg = Float.parseFloat(arr[2]);
                    m.Ksb = Float.parseFloat(arr[3]);
                } else if (arr[0].equals("Ke")) { // last one in file
                    m.Ker = Float.parseFloat(arr[1]);
                    m.Keg = Float.parseFloat(arr[2]);
                    m.Keb = Float.parseFloat(arr[3]);
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
			for (Material m : map.values()) return m;
			return null;
		}
	}
	
}
