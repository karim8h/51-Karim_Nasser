package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;


public class Line extends MyShapes {

	public Line() {

		setPropertiesNames(new String[2]);
		getPropertiesNames()[0] = "x2";
		getPropertiesNames()[1] = "y2";
		this.position = new Point(1,1);
		this.fillColor = Color.RED;
		this.lineColor = Color.RED;
		this.properties.put("x2", Double.valueOf(1));
		this.properties.put("y2", Double.valueOf(1));
	}
	@Override
	public void draw(Graphics canvas) {

		try{
			if(lineColor != null && canvas != null && position != null && properties != null)canvas.setColor(lineColor);
			if(lineColor != null && canvas != null && position != null && properties != null){
				int x2 = (int) properties.get(getPropertiesNames()[0]).doubleValue();
				int y2 = (int) properties.get(getPropertiesNames()[1]).doubleValue();
				canvas.drawLine(getPosition().x, getPosition().y,x2,y2);
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("error in drawing " + e.getMessage(), e);
		}
		
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	
		// check there is object to clone
		Shape temp = new Line();
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


