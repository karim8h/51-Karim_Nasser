package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;


public class Circle implements Shape {
	
	private Map<String, Double> properties = new HashMap<String,Double>();
	private String[] propertiesNames = new String[1];
	private Point position;
	private Color lineColor,fillColor;
	
	public Circle() {
		getPropertiesNames()[0] = "Radius";
		this.position = new Point(1,1);
		this.fillColor = Color.RED;
		this.lineColor = Color.RED;
		this.properties.put("Radius", Double.valueOf(1));
	}
	
	private String[] getPropertiesNames() {
		return propertiesNames;
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
				throw new RuntimeException("error int set prop");
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
	public void draw(Graphics canvas) {

		try{
			if(lineColor != null && canvas != null && position != null && properties != null)canvas.setColor(lineColor);
			if(lineColor != null && canvas != null && position != null && properties != null)canvas.drawOval(getPosition().x-this.getProperties()
					.get(getPropertiesNames()[0]).intValue() / 2, getPosition().y-this.getProperties()
					.get(getPropertiesNames()[0]).intValue() / 2, this.getProperties()
					.get(getPropertiesNames()[0]).intValue(), this.getProperties()
					.get(getPropertiesNames()[0]).intValue());
			
			if(fillColor != null && canvas != null && position != null && properties != null)canvas.setColor(fillColor);
			if(fillColor != null && canvas != null && position != null && properties != null)canvas.fillOval(getPosition().x-this.getProperties()
					.get(getPropertiesNames()[0]).intValue() / 2, getPosition().y-this.getProperties()
					.get(getPropertiesNames()[0]).intValue() / 2, this.getProperties()
					.get(getPropertiesNames()[0]).intValue(), this.getProperties()
					.get(getPropertiesNames()[0]).intValue());
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("error in drawing " + e.getMessage(), e);
		}
		
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		
		Shape temp = new Circle();
		if(this.getPosition() == null){
			temp.setPosition(null);
		}
		else{
			temp.setPosition((Point) this.getPosition().clone());
		}
		
		if (this.getColor() == null) {
			temp.setColor(null);
		}
		else{
			temp.setColor(new Color(this.getColor().getRGB()));
		}
		if (this.getFillColor() == null) {
			temp.setFillColor(null);
		}
		else{
			temp.setFillColor(new Color(this.getFillColor().getRGB()));
		}
		if (this.getProperties() == null) {
			temp.setProperties(null);
		}
		else{
			Map<String, Double> m = new HashMap<String, Double>();
			for(String s : this.getProperties().keySet()){
				m.put(s, this.getProperties().get(s));
			}
			temp.setProperties(m);
		}
			
		return temp;
	}

}
