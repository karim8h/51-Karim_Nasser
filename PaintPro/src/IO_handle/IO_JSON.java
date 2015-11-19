package IO_handle;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import draw_pac.Node;
import draw_pac.ShapeFactory;
import draw_pac.State;
import eg.edu.alexu.csd.oop.draw.Shape;

public class IO_JSON {
	
	private ArrayList<Node> list;
	private ShapeFactory factory;

	public void save(String path, Shape[] shapes) throws IOException{
		
		if(shapes == null)throw new RuntimeException("shapes array is null");
		File file = new File(path);
		
		BufferedWriter write = new BufferedWriter(new FileWriter(file));
		
		write.write("{");
		write.newLine();
		
		boolean flag = true;
		for(Shape shape : shapes){
			if(shapes == null)throw new RuntimeException("shape itself is null");
			if(flag){
				flag = false;
			}
			else{
				write.write(",");
				write.newLine();
			}
			print_object(shape, write);
		}
		
		write.newLine();
		write.write("}");
		
		write.close();
		
	}
	
	private void print_object(Shape shape, BufferedWriter write) throws IOException{
	//	try{
		Map<String, Double> prop;
		
		//shape name
		write.write("  \"" + shape.getClass().getName() + "\"" + ":");
		write.newLine();
		write.write("  {");
		write.newLine();
		
		//color
		if(shape.getColor() == null){
			write.write("    \"color\": " + "null");
		}
		else write.write("    \"color\": " + shape.getColor().getRGB());
		
		//fill color
		write.write(",");
		write.newLine();
		if(shape.getFillColor() == null){
			write.write("    \"fillColor\": " + "null");
		}
		else write.write("    \"fillColor\": " + shape.getFillColor().getRGB());
		
		//XPosition
		write.write(",");
		write.newLine();
		if(shape.getPosition() == null){
			write.write("    \"XPosition\": " + "null");
		}
		else write.write("    \"XPosition\": " + shape.getPosition().x);
		
		//YPosition
		write.write(",");
		write.newLine();
		if(shape.getPosition() == null){
			write.write("    \"YPosition\": " + "null");
		}
		else write.write("    \"YPosition\": " + shape.getPosition().y);
		
		//properties
		prop = shape.getProperties();
		if(prop == null){
			write.write(",");
			write.newLine();
			write.write("    \"" + "prop" + "\": " + "null");
		}
		else{
			Set<String> keys = prop.keySet();
			for(String key : keys){
				write.write(",");
				write.newLine();
				write.write("    \"" + key + "\": " + (int) prop.get(key).doubleValue());
			}
		}
		
		write.newLine();
		write.write("  }");
/*		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("error in print save  " + e.getMessage(), e);
		}
	*/	
	}
	
	public ArrayList<Node> load(String path) throws IOException{
		
		File file = new File(path);
		BufferedReader read = new BufferedReader(new FileReader(file));
		list = new ArrayList<Node>();
		factory = new ShapeFactory();
		
		if(!read.readLine().equals("{")) {
			read.close();
			throw new RuntimeException("wrong file");
		}
		try{
			while(true){
				boolean is_finish = read_shapes(read);
				if(is_finish)break;
			}
		}
		catch(Exception e){
			read.close();
			//e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		
		read.close();
		
		return list;
	}
	
	private boolean read_shapes(BufferedReader read) throws IOException{
		
		Map<String, Double> prop = new HashMap<String, Double>();
		
		String s;
		s = read.readLine().replaceAll("(\\s*\")(.+)(\":\\s*)", "$2");

		Shape shape = factory.createNewInstance(s);
		Point point = new Point();
		s = read.readLine();
		if(!s.matches("\\s*[{]")){
			throw new RuntimeException("wrong file");
		}
		
		while(!((s = read.readLine()).matches("\\s*},?\\s*"))){
			String s1, s2;
			
			s1 = s.replaceAll("(\\s*\")(.+)(\\s*\"\\s*:\\s*)(.+[^,])(,*\\s*)", "$2").trim();
			s2 = s.replaceAll("(\\s+\")(.+)(\\s*\"\\s*:\\s*)(.+[^,])(,*\\s*)", "$4").trim();
			
			if(s1.equals("color")){
				if(s2.equals("null")){
					shape.setColor(null);
				}
				else shape.setColor(new Color(Integer.parseInt(s2)));
			}
			else if(s1.equals("fillColor")){
				if(s2.equals("null")){
					shape.setFillColor(null);
				}
				else shape.setFillColor(new Color(Integer.parseInt(s2)));
			}
			else if(s1.equals("XPosition")){
				if(!s2.equals("null"))point.x = Integer.parseInt(s2);
			}
			else if(s1.equals("YPosition")){
				if(s2.equals("null")){
					point = null;
				}
				else point.y = Integer.parseInt(s2);
			}
			else{
				if(s2.equals("null")){
					prop = null;
				}
				else prop.put(s1, Double.parseDouble(s2));
			}
			
		}
		
		shape.setPosition(point);
		shape.setProperties(prop);
		list.add(new Node(State.ADDED, shape));
		
		if(s.matches("\\s*}")){
			s = read.readLine();
			return true;
		}
		return false;
		
	}
	
}
