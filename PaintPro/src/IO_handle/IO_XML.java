package IO_handle;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import draw_pac.Node;
import draw_pac.ShapeFactory;
import draw_pac.State;
import eg.edu.alexu.csd.oop.draw.Shape;

public class IO_XML {
	
	private ArrayList<Node> list = null;
	
	public void save(String path, Shape[] shapes) throws IOException{
				
		File file = new File(path);
		BufferedWriter write = new BufferedWriter(new FileWriter(file));
		
		write.write("<shapes>");
		write.newLine();
		
		for(Shape shape : shapes){
			
			write.write("<shape type=\"" + shape.getClass().getName() + "\">");
			write.newLine();
			
			//save border color
			write.write("<color>");write.newLine();
			if(shape.getColor() == null){
				write.write("null");
			}
			else write.write(String.valueOf(shape.getColor().getRGB()));
			write.newLine();
			write.write("</color>");write.newLine();
			
			//save fill color
			write.write("<fillColor>");write.newLine();
			if(shape.getFillColor() == null){
				write.write("null");
			}
			else write.write(String.valueOf(shape.getFillColor().getRGB()));
			write.newLine();
			write.write("</fillColor>");write.newLine();
			
			//save position
			write.write("<position>");write.newLine();
			write.write("<x>");write.newLine();
			if(shape.getPosition() == null){
				write.write("null");
			}
			else write.write(String.valueOf(shape.getPosition().x));write.newLine();
			write.write("</x>");write.newLine();
			write.write("<y>");write.newLine();
			if(shape.getPosition() == null){
				write.write("null");
			}
			else write.write(String.valueOf(shape.getPosition().y));write.newLine();
			write.write("</y>");write.newLine();
			write.write("</position>");write.newLine();
			
			//save properties
			Map<String, Double> prop = shape.getProperties();
			write.write("<properties>");
			write.newLine();
			if(shape.getProperties() == null){
				write.write("null");
				write.newLine();
			}
			else{
				Set<String> keys = prop.keySet();
				
				for(String key : keys){
					write.write("<" + key + ">");
					write.newLine();
					write.write(String.valueOf((int) prop.get(key).doubleValue()));
					write.newLine();
					write.write("</" + key + ">");
					write.newLine();
				}
			}
			
			write.write("</properties>");
			write.newLine();
			
			write.write("</shape>");write.newLine();
			
		}
		
		write.write("</shapes>");
		write.newLine();
		write.close();
	}
	
	public ArrayList<Node> load(String path){
		
		SAXParserFactory SAXFactory = SAXParserFactory.newInstance();
		SAXParser parser;
		list = null;
		try {
			parser = SAXFactory.newSAXParser();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("error in parser " + e.getMessage(), e);
		}
		
		SAXHandler handler = new SAXHandler();
		try {
			parser.parse(new FileInputStream(new File(path)), handler);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("error in parsing " + e.getMessage(), e);
		}
		
		return list;
	}
	
	private class SAXHandler extends DefaultHandler{
		
		private Shape shape = null;
		private Map<String, Double> prop = null;
		private ShapeFactory factory = new ShapeFactory();
		private String content = null;
		private Point point = null;
		
		public void startElement(String uri, String localName, String qname, Attributes attributes){
			
		//	System.out.println(qname);
			
			if(qname.equals("shapes")){
				list = new ArrayList<Node>();
			}
			else if(qname.equals("shape")){
				try{
					shape = factory.createNewInstance(attributes.getValue("type"));
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("class" + attributes.getValue("type") + "not found" + e.getMessage(), e);
				}
				
			}
			else if(qname.equals("properties")){
				prop = new HashMap<String, Double>();
			}
			else if(qname.equals("position")){
				point = new Point();
			}
			
		}
		
		public void endElement(String uri, String localName, String qname){
						
			if(qname.equals("shape")){
				list.add(new Node(State.ADDED, shape));
			}
			else if(qname.equals("color")){
				if(content.equals("null")){
					shape.setColor(null);
				}
				else shape.setColor(new Color(Integer.parseInt(content)));
			}
			else if(qname.equals("fillColor")){
				if(content.equals("null")){
					shape.setFillColor(null);
				}
				else shape.setFillColor(new Color(Integer.parseInt(content)));
			}
			else if(qname.equals("x")){

				if(!content.equalsIgnoreCase("null"))point.x = Integer.parseInt(content);
			}
			else if(qname.equals("y")){
				if(content.equals("null")){
					point = null;
				}
				else point.y = Integer.parseInt(content);
			}
			else if(qname.equals("properties")){
				if(content.equals("null")){
					shape.setProperties(null);
				}
				else shape.setProperties(prop);
			}
			else if(qname.equals("position")){
				shape.setPosition(point);
			}
			else if(!qname.equals("shapes")){

				if(!content.equals("null"))prop.put(qname, Double.valueOf(Double.parseDouble(content)));
			}
			
		}
		
		public void characters(char[] ch, int start, int length){
			content = String.copyValueOf(ch, start, length).trim();
		}
		
	}

}
