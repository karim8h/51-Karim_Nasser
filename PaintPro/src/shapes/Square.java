package shapes;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;

public class Square extends Rectangle {
	public Square(){
		getPropertiesNames()[0] = "SideLength";
		getPropertiesNames()[1] = "SideLength";
		this.position = new Point(1,1);
		this.fillColor = Color.RED;
		this.lineColor = Color.RED;
		this.properties.remove("Width");
		this.properties.remove("Height");
		this.properties.put("SideLength", Double.valueOf(1));
	}

	@Override
	public Object clone() throws CloneNotSupportedException {

		// check there is object to clone
		Shape temp = new Square();
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
