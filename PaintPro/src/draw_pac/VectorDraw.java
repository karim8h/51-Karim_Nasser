package draw_pac;

import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;
import IO_handle.IO_JSON;
import IO_handle.IO_XML;
import shapes.Circle;
import shapes.Ellipse;
import shapes.Line;
import shapes.Rectangle;
import shapes.Square;
import shapes.Triangle;

public class VectorDraw extends Observable implements DrawingEngine {
	
	private ArrayList<Node> shapes = new ArrayList<Node> ();
	private ArrayList<Node> history = new ArrayList<Node>();
	private Node test = new Node();
	private IO_XML io_xml = new IO_XML();
	private IO_JSON io_json = new IO_JSON();
	private int max_size = 20;
	private int last =  -1;
	
	@Override
	public void refresh(Graphics canvas) {
		
		Shape[] shape_arr = getShapes();
		
			for(Shape shape : shape_arr){
				shape.draw(canvas);
			}
		
	}

	@Override
	public void addShape(Shape shape) {
		
		Shape new_shape = shape;

		if(last + 1 >= max_size){
			history.remove(0);
			last--;
		}
		try{
			while(history.size() > last + 1)history.remove(history.size() - 1);
			history.add(new Node(State.ADDED, new_shape));
			last++;
			
			shapes.add(new Node(State.ADDED, new_shape));
		}catch(Exception e){
			//e.printStackTrace();
			throw new RuntimeException("error in add " + e.getMessage(), e);
		}

		this.tell_observers();
	}

	@Override
	public void removeShape(Shape shape) {		
		
		Shape new_shape = shape;
		
			int index = test.index_of(shapes, new Node(State.ADDED, new_shape));
			if(index == -1)throw new RuntimeException("invalid operation");
			shapes.remove(index);
			shapes.add(index, new Node(State.DELETD, new_shape));
			
			if(last + 1 >= max_size){
				history.remove(0);
				last--;
			}
			
			while(history.size() > last + 1)history.remove(history.size() - 1);
			
			history.add(new Node(State.DELETD, new_shape));
			last++;
		
		this.tell_observers();
	}

	@Override
	public void updateShape(Shape oldShape, Shape newShape) {
		
		Shape new_shape, old_shape;
		try{
			new_shape = (Shape) newShape.clone();
			old_shape = (Shape) oldShape.clone();
		}catch(Exception e){
			//e.printStackTrace();
			throw new RuntimeException("error in clone in update " + e.getMessage(), e);
		}
		
			int index = test.index_of(shapes, new Node(State.ADDED, old_shape));
			if(index == -1)throw new RuntimeException("invalid operation");
			shapes.remove(index);
			shapes.add(index, new Node(State.UPDATED, old_shape));
			shapes.add(index + 1, new Node(State.ADDED, new_shape));
			
			if(last + 1 >= max_size){
				history.remove(0);
				last--;
			}
			


			while(history.size() > last + 1)history.remove(history.size() - 1);
			
			history.add(new Node(State.UPDATED, old_shape));
			last++;
		
		this.tell_observers();
	}

	@Override
	public Shape[] getShapes() {
					
		ArrayList<Shape> list_shapes = new ArrayList<Shape>();
				
		for(Node shape : shapes){
			if(shape.get_state() == State.ADDED){
				list_shapes.add(shape.get_shape());
			}
			
		}
		if(list_shapes.size() == 0){
			return new Shape[0];
		}
		int i = 0;
		Shape[] arr_shapes = new Shape[list_shapes.size()];

		for(Shape shape : list_shapes){
			try {
				if(shape == null)arr_shapes[i] = null;
				else arr_shapes[i] = (Shape) shape.clone();
			} catch (Exception e ) {
				arr_shapes[i] = shape;
			}
			i++;
		}

		
		return arr_shapes;
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Class<? extends Shape>> getSupportedShapes() {
		
	//	System.setProperty("java.class.path", "C:\\Users\\KARIM\\Desktop\\nb\\pol.jar");
		List<Class<? extends Shape>> list = new ArrayList<Class<? extends Shape>>();
		try{
			list.add(Circle.class);
			list.add(Ellipse.class);
			list.add(Line.class);
			list.add(Rectangle.class);
			list.add(Square.class);
			list.add(Triangle.class);
			
			String path = System.getProperty("java.class.path");
			String[] cp = path.split(Character.toString(File.pathSeparatorChar));
			
			for(String s : cp){
				//System.out.println(s);
				if(s.matches(".+\\.[jJ][aA][rR]")){
					File f = new File(s);
					if(!f.exists())continue;
					JarInputStream j = new JarInputStream(new FileInputStream(f));
					JarEntry e = j.getNextJarEntry();
					
			//		System.out.println(e.getName());
					
					if(!e.getName().matches(".+\\.class"))continue;
					String className = e.getName().replaceAll("(.+)(\\.class)", "$1");
					className = className.replace('/', '.');
					
			//		System.out.println(className);
					
					ClassLoader cl = Shape.class.getClassLoader();
					
					Class c = cl.loadClass(className);
					
					if(Shape.class.isAssignableFrom(c)){
						list.add(c);
					}
					j.close();
				}
				
			}
			
		}
		catch(Exception e){
			//e.printStackTrace();
			throw new RuntimeException("error using reflection " + e.getMessage(), e);
		}
		
		return list;
	}

	@Override
	public void undo() {
		
		if(last < 0)throw new RuntimeException("no undo available");
		
		Node tmp = history.get(last);
		last--;
		if(tmp.get_state() == State.ADDED){
			
			int index = test.index_of(shapes, new Node(State.ADDED, tmp.get_shape()));
			if(index == -1)throw new RuntimeException("error");
			shapes.remove(index);
			shapes.add(index, new Node(State.DELETD, tmp.get_shape()));
		}
		else if(tmp.get_state() == State.DELETD){
			int index = test.index_of(shapes, new Node(State.DELETD, tmp.get_shape()));
			if(index == -1)throw new RuntimeException("error");
			shapes.remove(index);
			shapes.add(index, new Node(State.ADDED, tmp.get_shape()));
		}
		else{
			int index = test.index_of(shapes, new Node(State.UPDATED, tmp.get_shape()));
			if(index == -1)throw new RuntimeException("error");
			if(index >= shapes.size() || shapes.get(index + 1).get_state() != State.ADDED){
				throw new RuntimeException("error");
			}
			
			shapes.remove(index);
			shapes.add(index, new Node(State.ADDED, tmp.get_shape()));
			Node tmp2 = shapes.get(index + 1);
			if(tmp2.get_state() != State.ADDED)throw new RuntimeException("error");
			shapes.remove(index + 1);
			shapes.add(index + 1, new Node(State.DELETD, tmp2.get_shape()));
		}
		
		this.tell_observers();
	}

	@Override
	public void redo() {

		if(last + 1 >= history.size()){
			throw new RuntimeException("unvalid operation");
		}
		
		last++;
		Node tmp = history.get(last);
		
		if(tmp.get_state() == State.ADDED){
			
			int index = test.index_of(shapes, new Node(State.DELETD, tmp.get_shape()));
			if(index == -1)throw new RuntimeException("error");
			shapes.remove(index);
			shapes.add(index, new Node(State.ADDED, tmp.get_shape()));
		}
		else if(tmp.get_state() == State.DELETD){
			int index = test.index_of(shapes, new Node(State.ADDED, tmp.get_shape()));
			if(index == -1)throw new RuntimeException("error");
			shapes.remove(index);
			shapes.add(index, new Node(State.DELETD, tmp.get_shape()));
		}
		else{
			int index = test.index_of(shapes, new Node(State.ADDED, tmp.get_shape()));
			if(index == -1)throw new RuntimeException("error");
			if(index >= shapes.size() || shapes.get(index + 1).get_state() != State.DELETD){
				throw new RuntimeException("error");
			}
			
			shapes.remove(index);
			shapes.add(index, new Node(State.UPDATED, tmp.get_shape()));
			Node tmp2 = shapes.get(index + 1);
			if(tmp2.get_state() != State.DELETD)throw new RuntimeException("error");
			shapes.remove(index + 1);
			shapes.add(index + 1, new Node(State.ADDED, tmp2.get_shape()));
		}
		
		this.tell_observers();
	}

	@Override
	public void save(String path) {
		
		try {
			if(path.matches(".+\\.[xX][mM][lL]")){
				path = path.replaceAll("(.+\\.)([xX][mM][lL])", "$1");
				path += "xml";
				io_xml.save(path, getShapes());
			}
			else if(path.matches(".+\\.[jJ][sS][oO][nN]")){
				path = path.replaceAll("(.+\\.)([jJ][sS][oO][nN])", "$1");
				path += "json";
				io_json.save(path, getShapes());
			}
			else{
				throw new RuntimeException("only *.xml and *.json file types are supported");
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
	}

	@Override
	public void load(String path) {
		
		try {
			if(path.matches(".+\\.[xX][mM][lL]")){
				path = path.replaceAll("(.+\\.)([xX][mM][lL])", "$1");
				path += "xml";
				shapes = io_xml.load(path);
			}
			else if(path.matches(".+\\.[jJ][sS][oO][nN]")){
				path = path.replaceAll("(.+\\.)([jJ][sS][oO][nN])", "$1");
				path += "json";
				shapes = io_json.load(path);
			}
			else{
				throw new RuntimeException("only *.xml and *.json file types are supported");
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
		history = new ArrayList<Node>();
		last = -1;
	
		this.tell_observers();
	}
	
	private void tell_observers(){
		
		ArrayList<Shape> list = new ArrayList<Shape>();
		
		for(Node theShape : shapes){
			if(theShape.get_state() == State.ADDED){
				list.add(theShape.get_shape());
			}
		}
		
		setChanged();
		notifyObservers(list);
	}
	
	
		
}
