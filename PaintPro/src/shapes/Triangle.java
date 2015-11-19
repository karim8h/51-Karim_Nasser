package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;

public class Triangle extends MyShapes{
	public Triangle() {

		setPropertiesNames(new String[4]);
		getPropertiesNames()[0] = "x2";
		getPropertiesNames()[1] = "y2";
		getPropertiesNames()[2] = "x3";
		getPropertiesNames()[3] = "y3";
		this.position = new Point(1,1);
		this.fillColor = Color.RED;
		this.lineColor = Color.RED;
		this.properties.put("x2", Double.valueOf(1));
		this.properties.put("y2", Double.valueOf(1));
		this.properties.put("x3", Double.valueOf(1));
		this.properties.put("y3", Double.valueOf(1));
	}
	@Override
	public void draw(Graphics canvas) {

		try{
			int[] xPoints = new int[3];
			int[] yPoints = new int[3];
			if(canvas != null && position != null && properties != null){
				xPoints[0] = getPosition().x;
				xPoints[1] = getProperties().get(getPropertiesNames()[0]).intValue();
				xPoints[2] = getProperties().get(getPropertiesNames()[2]).intValue();
				yPoints[0] = getPosition().y;
				yPoints[1] = getProperties().get(getPropertiesNames()[1]).intValue();
				yPoints[2] = getProperties().get(getPropertiesNames()[3]).intValue();
			}
			if(lineColor != null && canvas != null && position != null && properties != null)canvas.setColor(lineColor);
			if(lineColor != null && canvas != null && position != null && properties != null)canvas.drawPolygon(xPoints,yPoints, 3);
			if(fillColor != null && canvas != null && position != null && properties != null)canvas.setColor(fillColor);
			if(fillColor != null && canvas != null && position != null && properties != null)canvas.fillPolygon(xPoints, yPoints, 3);
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("error in drawing " + e.getMessage(), e);
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {

		// check there is object to clone
		Shape temp = new Triangle();
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
