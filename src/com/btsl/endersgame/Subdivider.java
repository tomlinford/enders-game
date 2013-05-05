package com.btsl.endersgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Not thread safe! Can only subdivide one model at a time because
 * face and vertex data for a model are stored in globals.
 * @author bhnascar
 *
 */
public class Subdivider {
	
	/* List of faces */
	private static ArrayList<Face> faces = new ArrayList<Face>();
	
	/* Map of vertices 
	   Key: position as a vec3
	   Value: pointer to a Vertex struct */
	private static HashMap<Position, Vertex> vertices = new HashMap<Position, Vertex>();
	
	public static void Subdivide(OBJFile model) {
		if (faces.size() == 0)
	        GenerateAdjacencyInformation(model.vertCoords,
	        							 model.normals,
	        							 model.texCoords,
	        							 model.indices);
	    
	    if (faces.size() > 0) {
	        DivideFaces();
	        ExportToOBJFile(model);
	    }
	}
	
	/* Generates mesh information (triangles and vertices) */
	private static void GenerateAdjacencyInformation(ArrayList<Float> vertCoords, 
			ArrayList<Float> texCoords, ArrayList<Float> normalCoords, 
			ArrayList<Integer> vertexIndices) 
	{
	    // Add faces and vertices
	    for (int i = 0; i < vertexIndices.size(); i += 3)
	    {
	        // Create first vertex
	    	Vertex v1 = AddVertex(vertCoords.get(3 * vertexIndices.get(i)),
	    						  vertCoords.get(3 * vertexIndices.get(i) + 1),
	    						  vertCoords.get(3 * vertexIndices.get(i) + 2));
	        
	        // Create second vertex
	        Vertex v2 = AddVertex(vertCoords.get(3 * vertexIndices.get(i + 1)),
					  			  vertCoords.get(3 * vertexIndices.get(i + 1) + 1),
					  			  vertCoords.get(3 * vertexIndices.get(i + 1) + 2));

	        // Create third vertex
	        Vertex v3 = AddVertex(vertCoords.get(3 * vertexIndices.get(i + 2)),
		  			  			  vertCoords.get(3 * vertexIndices.get(i + 2) + 1),
		  			  			  vertCoords.get(3 * vertexIndices.get(i + 2) + 2));

	        // Create new face and add to global list
	        Face face = new Face(v1, v2, v3);
	        faces.add(face);
	    }
	    
	    // Calculate face's neighbors recursively
	    System.out.println("Faces found: " + faces.size());
	    if (faces.size() > 0)
	        FindNeighbors(faces);
	}
	
	/**
	 * Adds new vertices and creates new faces.
     * The new faces are stored in a new vector<Face *>,
     * and swapped with the existing vector when the
     * method is finished.
	 */
	private static void DivideFaces() {
		// Add new faces
	    ArrayList<Face> subdividedFaces = new ArrayList<Face>();
	    for (Face face : faces) {
	        // Add new (odd) vertices at midpoints along face's edges
	        Vertex v1_v2 = AddVertex(face.v1, face.v2);
	        Vertex v2_v3 = AddVertex(face.v2, face.v3);
	        Vertex v3_v1 = AddVertex(face.v3, face.v1);
	        
	        // Skip over deformed faces (lines) with duplicated vertices
	        if (v1_v2.equals(v2_v3) || v2_v3.equals(v3_v1) || v1_v2.equals(v3_v1))
	            continue;
	        
	        // Displace new (odd) vertices
	        //DisplaceOddVertex(v1_v2, face.v1, face.v2, face.v3, face.n1);
	        //DisplaceOddVertex(v2_v3, face.v2, face.v3, face.v1, face.n2);
	        //DisplaceOddVertex(v3_v1, face.v3, face.v1, face.v2, face.n3);
	        
	        // Generate new faces
            subdividedFaces.add(new Face(v1_v2, v2_v3, v3_v1));
            subdividedFaces.add(new Face(face.v1, v1_v2, v3_v1));
            subdividedFaces.add(new Face(v1_v2, face.v2, v2_v3));
            subdividedFaces.add(new Face(v3_v1, v2_v3, face.v3));
	    }
	    
	    // Delete old faces from vertices. This must be done
	    // as a separate pass from the above loop because
	    // odd vertex displacement traverses the old face
	    // structure to find reference vertices
	    for (Face face : faces) {
	        RemoveFace(face.v1, face);
	        RemoveFace(face.v2, face);
	        RemoveFace(face.v3, face);
	    }
	    
	    // Recompute adjacency information
	    FindNeighbors(subdividedFaces);
	    
	    // Displace even vertices
	    /*for (Vertex vertex : vertices.values()) {
	        if (vertex.index != -2)
	        	DisplaceEvenVertex(vertex);
	    }*/
	    
	    // Swap face information
	    faces = subdividedFaces;
	}
	
	/* Exports the subdivided data to the given model */
	private static void ExportToOBJFile(OBJFile model) {
		ArrayList<Float> model_vertices = model.vertCoords;
	    ArrayList<Integer> model_faces = model.indices;
	    
	    model_vertices.clear();
	    model_faces.clear();
	    
	    // Push vertices to buffer and assign indices to them
	    int index = 0;
	    for (Vertex vertex : vertices.values()) {
	        if (vertex == null)
	            continue;
	        
	        model_vertices.add(vertex.position.x);
	        model_vertices.add(vertex.position.y);
	        model_vertices.add(vertex.position.z);
	        vertex.index = index++;
	    }
	    
	    // Push vertex indices (faces) to buffer
	    for (Face face : faces) {
	    	System.out.println(face);
	    	model_faces.add(face.v1.index);
	    	model_faces.add(face.v2.index);
	    	model_faces.add(face.v3.index);
	    }
	}
	
	/* Vertex displacement methods */
	
	/**
	 *  Displaces an odd vertices using mask.
	 *  
	 *  	  1/8
	 *       /   \
	 *  	/     \
	 *     /       \
	 *   3/8   v   3/8
	 *     \       /
	 *      \     /
	 *       \   /
	 *        1/8
	 *        
	 * @param v
	 * @param v_left
	 * @param v_right
	 * @param v_opposite
	 * @param neighbor
	 */
	private static void DisplaceOddVertex(Vertex v,
	                                   	  Vertex v_left,
	                                      Vertex v_right,
	                                      Vertex v_opposite,
	                                      Face neighbor)
	{
		// No neighbors: special case
	    if (neighbor == null) {
	        v.position = new Position(v_left.position.x * 0.5f + v_right.position.x * 0.5f,
	        						  v_left.position.y * 0.5f + v_right.position.y * 0.5f,
	        						  v_left.position.z * 0.5f + v_right.position.z * 0.5f);
	        return;
	    }
	    
	    // Found neighbors, calculate v's new x, y, z using mask:
	    float x = (3.0f / 8.0f) * v_left.position.x + (3.0f / 8.0f) * v_right.position.x +
	    		  (1.0f / 8.0f) * v_opposite.position.x;
	    float y = (3.0f / 8.0f) * v_left.position.x + (3.0f / 8.0f) * v_right.position.x +
		    	  (1.0f / 8.0f) * v_opposite.position.x;
	    float z = (3.0f / 8.0f) * v_left.position.x + (3.0f / 8.0f) * v_right.position.x +
		    	  (1.0f / 8.0f) * v_opposite.position.x;
	    
	    if (neighbor.v1 != v_left && neighbor.v1 != v_right) {
	        x += (1.0f / 8.0f) * neighbor.v1.position.x;
	        y += (1.0f / 8.0f) * neighbor.v1.position.y;
	        z += (1.0f / 8.0f) * neighbor.v1.position.z;
	    }
	    else if (neighbor.v2 != v_left && neighbor.v2 != v_right) {
	    	x += (1.0f / 8.0f) * neighbor.v2.position.x;
	        y += (1.0f / 8.0f) * neighbor.v2.position.y;
	        z += (1.0f / 8.0f) * neighbor.v2.position.z;
	    }
	    else if (neighbor.v3 != v_left && neighbor.v3 != v_right) {
	    	x += (1.0f / 8.0f) * neighbor.v3.position.x;
	        y += (1.0f / 8.0f) * neighbor.v3.position.y;
	        z += (1.0f / 8.0f) * neighbor.v3.position.z;
	    }
	    
	    v.position = new Position(x, y, z);
	}
	
	/**
	 * Displaces an even vertex using a mask. 
	 * This must be called after adjacency information
	 * has been generated for the odd vertices,
	 * and all odd vertices have been displaced. 
	 * 
	 *  	  1/n-------1/n
	 *       /   \     /   \
	 *  	/     \   /     \
	 *     /       \ /       \
	 *   1/n       1/n       1/n
	 *     \       / \       /
	 *      \     /   \     /
	 *       \   /     \   /
	 *        1/n-------1/n
	 * 
	 * @param v
	 */
	private static void DisplaceEvenVertex(Vertex v)
	{
	    // Collect surrounding odd vertices
	    HashSet<Vertex> vertices = new HashSet<Vertex>();
	    for (int i = 0; i < v.faces.size(); i++)
	    {
	        Face face = v.faces.get(i);
	        
	        vertices.add(face.v1);
	        vertices.add(face.v2);
	        vertices.add(face.v3);
	    }
	    
	    // Calculate weight
	    float n = vertices.size();
	    @SuppressWarnings("unused")
		float beta;
	    if (n == 3)
	        beta = 3.0f / 16.0f;
	    else
	        beta = 3.0f / (8.0f * n);
	    
	    // Calculate new position
	    float x, y, z;
	    x = y = z = 0.0f;
	    for (Vertex vertex : vertices) {
	        /* if (vertex == v) {
	            x += (1.0f - n * beta) * vertex.position.x;
	            y += (1.0f - n * beta) * vertex.position.y;
	            z += (1.0f - n * beta) * vertex.position.z;
	        }
	        else {
	            x += beta * vertex.position.x; 
	            y += beta * vertex.position.y;
	            z += beta * vertex.position.z;
	        }*/
	        x += (1.0f / n) * vertex.position.x;
	        y += (1.0f / n) * vertex.position.y;
	        z += (1.0f / n) * vertex.position.z;
	    }
	    v.position = new Position(x, y, z);
	}
	
	/* Helper methods for repeated tasks */
	
	/**
	 * Adds a new vertex at the midpoint of the two given ones.
	 * @param v1
	 * @param v2
	 */
	private static Vertex AddVertex(Vertex v1, Vertex v2) {
		Vertex v = AddVertex((v1.position.x + v2.position.x) / 2,
						 	 (v1.position.y + v2.position.y) / 2,
						 	 (v1.position.z + v2.position.z) / 2);
		v.index = -2;
		return v;
	}
	
	/**
	 * Adds a new vertex for the given x,y,z location.
	 * @param x
	 * @param y
	 * @param z
	 */
	private static Vertex AddVertex(float x, float y, float z) {
		Vertex vertex = vertices.get(new Position(x, y, z));
		if (vertex == null) {
			vertex = new Vertex(x, y, z);
			vertices.put(vertex.position, vertex);
		}
		return vertex;
	}
	
	/**
	 * Finds and removes this face from the given vertex's list of
	   associated faces. Linear search.
	 * @param vertex
	 * @param face
	 */
	private static void RemoveFace(Vertex vertex, Face face)
	{
	    for (int i = 0; i < vertex.faces.size(); i++)
	    {
	    	Face f = vertex.faces.get(i);
	        if (f.equals(face)) {
	            vertex.faces.remove(f);
	            return;
	        }
	    }
	}
	
	/**
	 * Finds the neighbor for a given face across the edge defined by
	 * v1 and v2.
	 * @param face
	 * @param neighbor
	 * @param v1
	 * @param v2
	 */
	private static void FindNeighbor(Face face, int neighbor, Vertex v1, Vertex v2) {
		
		Face n = null;
		
		// Loop through vertices' associated faces
	    for (int i = 0; i < v1.faces.size(); i++)
	    {
	        Face other = v1.faces.get(i);
	        
	        // Skip over the given face if it's us
	        // (don't make a face neighbors with itself)
	        if (other == face)
	            continue;
	        
	        // If the face contains both v1 and v2,
	        // it's our neighbor and we're it's neighbor
	        n = other;
	        if (other.v1 == v2) {
	            other.n1 = face;
	            // Assign neighbor as appropriate
	    	    if (neighbor == 1)
	    	    	face.n1 = n;
	    	    else if (neighbor == 2)
	    	    	face.n2 = n;
	    	    else if (neighbor == 3)
	    	    	face.n3 = n;
	    	    return;
	        }
	        else if (other.v2 == v2) {
	            other.n2 = face;
	            // Assign neighbor as appropriate
	    	    if (neighbor == 1)
	    	    	face.n1 = n;
	    	    else if (neighbor == 2)
	    	    	face.n2 = n;
	    	    else if (neighbor == 3)
	    	    	face.n3 = n;
	    	    return;
	        }
	        else if (other.v3 == v2) {
	            other.n3 = face;
	         // Assign neighbor as appropriate
	    	    if (neighbor == 1)
	    	    	face.n1 = n;
	    	    else if (neighbor == 2)
	    	    	face.n2 = n;
	    	    else if (neighbor == 3)
	    	    	face.n3 = n;
	    	    return;
	        }
	    }
	}
	
	/**
	 * Finds the neighbors for all the faces in the given list of
	 * faces.
	 * @param faces
	 */
    private static void FindNeighbors(ArrayList<Face> faces) {
    	for (Face face : faces) {
            // Find the neighbors
            FindNeighbor(face, 1, face.v1, face.v2);
            FindNeighbor(face, 2, face.v2, face.v3);
            FindNeighbor(face, 3, face.v3, face.v1);
        }
    }

	/* Private helper classes for mainting mesh data */
	
    /**
     * Defines a mesh face. A face is composed of three vertices
     * and three neighbors across the three edges defined by those
     * vertices.
     * @author bhnascar
     *
     */
	private static class Face {
		
		public Vertex v1;
		public Vertex v2;
		public Vertex v3;
		public Face n1; 	// Neighbor across v1-v2
		public Face n2; 	// Neighbor across v2-v3
		public Face n3; 	// Neighbor across v3-v1
		
		public Face(Vertex v1, Vertex v2, Vertex v3) {
			this.v1 = v1;
			this.v2 = v2;
			this.v3 = v3;
			
			v1.faces.add(this);
			v2.faces.add(this);
			v3.faces.add(this);
		}
		
		/**
		 * Compares two faces by constituent vertices.
		 */
		public boolean equals(Object other) {
			if (other instanceof Face) {
				Face f = (Face)other;
				return this.v1.equals(f.v1) &&
					   this.v2.equals(f.v2) &&
					   this.v3.equals(f.v3);
			}
			return false;
		}
		
		public String toString() {
			String str = "Face at (" + v1.position.x + ", " + v1.position.y + ", " + v1.position.z + ") " +
						 "(" + v2.position.x + ", " + v2.position.y + ", " + v2.position.z + ") " +
						 "(" + v3.position.x + ", " + v3.position.y + ", " + v3.position.z + ") "; 
			return str;
		}
		
	}
	
	/**
	 * Defines a mesh vertex. A vertex has a position and a list
	 * of faces to which it belongs.
	 * @author bhnascar
	 *
	 */
	private static class Vertex implements Comparable<Object> {
		
		public int index;
		public Position position; 	  // Position
		public ArrayList<Face> faces = new ArrayList<Face>(); // Associated faces
		
		public Vertex(float x, float y, float z) {
			this.position = new Position(x, y, z);
			this.index = -1;
		}
		
		/**
		 * Compares two vertices by position.
		 */
		@Override
		public int compareTo(Object other) {
			if (other instanceof Vertex) {
				Vertex v = (Vertex)other;
				this.position.compareTo(v.position);
			}
			return -1;
		}
		
		/**
		 * Compares two vertices by position.
		 */
		@Override
		public boolean equals(Object other) {
			if (other instanceof Vertex) {
				Vertex v = (Vertex)other;
				return this.position.equals(v.position);
			}
			return false;
		}
		
	}
	
	/**
	 * An x, y, z, position. We use this class to represent a position
	 * rather than an int[3] because we need to override float
	 * comparison to use positions as keys in a HashMap.
	 * @author bhnascar
	 *
	 */
	private static class Position implements Comparable<Object> {
		
		public float x, y, z;
		
		public Position(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		/**
		 * Eclipse-generated, lets us use a HashMap
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + discretize(x);
			result = prime * result + discretize(y);
			result = prime * result + discretize(z);
			return result;
		}
		
		/**
		 * Compares two positions by x,y,z values.
		 */
		@Override
		public int compareTo(Object other) {
			if (other instanceof Position) {
				Position p = (Position)other;
				if (discretize(this.x) != discretize(p.x)) {
					return discretize(this.x) - discretize(p.x);
				}
				else if (discretize(this.y) != discretize(p.y)) {
					return discretize(this.y) - discretize(p.y);
				}
				else if (discretize(this.z) != discretize(p.z)) {
					return discretize(this.z) - discretize(p.z);
				}
				return 0;
			}
			return -1;
		}
		
		/**
		 * Compares two positions by x,y,z values.
		 */
		@Override
		public boolean equals(Object other) {
			if (other instanceof Position) {
				Position p = (Position)other;
				return (discretize(this.x) == discretize(p.x) &&
						discretize(this.y) == discretize(p.y) &&
						discretize(this.z) == discretize(p.z));
			}
			return false;
		}
		
		/**
		 * Converts a float to an int, preserving 5 digits of 
		 * decimal precision.
		 */
		private int discretize(float f) {
			return (int)(f * 100000);
		}
	}
	
}
