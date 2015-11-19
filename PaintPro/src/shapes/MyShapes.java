package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;

public abstract class MyShapes implements Shape {
	
	protected Map<String, Double> properties = new HashMap<String,Double>();
	private String[] propertiesNames = new String[4];
	protected Point position;
	protected Color lineColor,fillColor;
	
	protected String[] getPropertiesNames() {
		return propertiesNames;
	}
	
	protected void setPropertiesNames(String[] propertiesNames) {
		this.propertiesNames = propertiesNames;
	}
	
	@Override
	public void setPosition(Point position) {
		
		this.position = position;
	}

	@Override
	public Point getPosition() {
		
		return position;
	}

	@Override
	public void setProperties(Map<String, Double> properties) {
		
		if(properties == null){
			this.properties = null;
			return;
		}
		for (int i = 0; i < propertiesNames.length; i++) {
			// check existence of keys
			if (!properties.containsKey(propertiesNames[i])) {
				throw new RuntimeException("error in set prop");
			}

			// check values of keys
			if (properties.get(propertiesNames[i])== null) {
				throw new RuntimeException("error in set prop");

			}
		}
		//this.properties = (Map<String,Double>)((HashMap)properties).clone();
		//don't do that clone because it is depending on that coming properties is HashMap
		//and there no assumption on that
		// should do iterator ask for it
		for (String str : properties.keySet()) {
			this.properties.put(str, properties.get(str));
		}
	}

	@Override
	public Map<String, Double> getProperties() {

		// put map keys
		if(this.properties == null)return null;
		for (int i = 0; i < propertiesNames.length; i++) {
			if (!properties.containsKey(propertiesNames[i])) {
				properties.put(propertiesNames[i], null);
			}
		}
		return properties;
	}

	

	@Override
	public void setColor(Color color) {
		
		if(color == null){
			this.lineColor = null;
			return;
		}
		lineColor = new Color(color.getRGB());
	}

	@Override
	public Color getColor() {

		return lineColor;
	}

	@Override
	public void setFillColor(Color color) {
		
		if(color == null){
			this.fillColor = null;
			return;
		}
		fillColor = new Color(color.getRGB());
	}

	@Override
	public Color getFillColor() {

		return fillColor;
	}

	@Override
	abstract public void draw(Graphics canvas);
	abstract public Object clone() throws CloneNotSupportedException ;
	
}
