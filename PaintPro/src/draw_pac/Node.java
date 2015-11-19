package draw_pac;

import java.util.ArrayList;

import eg.edu.alexu.csd.oop.draw.Shape;

public class Node{
	
	private State state;
	private Shape shape;
	
	public Node(State state, Shape shape){
		this.state = state;
		this.shape = shape;
	}
	
	public Node() {

	}
	
	public void set_state(State state){
		this.state = state;
	}
	public State get_state(){
		return state;
	}
	public void set_shape(Shape shape){
		this.shape = shape;
	}
	public Shape get_shape(){
		return shape;
	}

	private boolean is_equal(Node a, Node b){
		
		try{
				
		if(a.state.equals(b.state) && a.shape != null && b.shape != null
				 && (a.shape.getColor() == b.shape.getColor() || (a.shape.getColor() != null && b.shape.getColor() != null && a.shape.getColor().equals(b.shape.getColor())))
				 && (a.shape.getFillColor() == b.shape.getFillColor() || (a.shape.getFillColor() != null && b.shape.getFillColor() != null && a.shape.getFillColor().equals(b.shape.getFillColor())))
				 && (a.shape.getPosition() == b.shape.getPosition() ||(a.shape.getPosition() != null && b.shape.getPosition() != null &&  a.shape.getPosition().equals(b.shape.getPosition())))
				 && (a.shape.getProperties() == b.shape.getProperties() || (a.shape.getProperties() != null && b.shape.getProperties() != null && a.shape.getProperties().equals(b.shape.getProperties())))){
			return true;
		}
		return false;
		}catch(Exception e){
			e.printStackTrace();;
			throw new RuntimeException("error in node " + e.getMessage(), e);
		}
	}
	
	public int index_of(ArrayList<Node> list, Node node){
		
		int i = 0;
		for(Node tmp : list){
			if(is_equal(tmp, node)){
				return i;
			}
			i++;
		}
		
		return -1;
	}
	
	
}
