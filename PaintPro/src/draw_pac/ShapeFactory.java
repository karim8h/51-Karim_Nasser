package draw_pac;

import eg.edu.alexu.csd.oop.draw.Shape;


public class ShapeFactory {
	
	public Shape createNewInstance(String s){
		
		Class<?> c;
		Shape shape;
		try{
			c = Class.forName(s);
			shape = (Shape) c.newInstance();
		}
		catch(Exception e){
			throw new RuntimeException("class not found");
		}
		
		return shape;
		
	}
	
}
