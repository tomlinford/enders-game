package com.btsl.endersgame;

import java.util.ArrayList;

public class Model {

	// List of components that make up this model
	private ArrayList<Component> components;
	
	// Overall model orientation data
	public float[] orientation;
	
	public Model() {
		components = new ArrayList<Component>();
	}
	
	public void AddComponent(Component component) {
		components.add(component);
	}
	
	public void Draw(Program program, int mode) {
		for (Component component : components)
			component.Draw(program, mode);
	}
	
}
