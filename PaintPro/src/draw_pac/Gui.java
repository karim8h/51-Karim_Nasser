package draw_pac;

import eg.edu.alexu.csd.oop.draw.*;
import shapes.*;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.awt.event.*;


public class Gui extends JFrame{
	
	private static final long serialVersionUID = 1L;

	public static void main(String args[]){
		new Gui();
	}
	
	
	
	private JComboBox<String> choose_shape;
	ShapeFactory factory;
	private JButton select;
	private JButton choose_col;
	private JButton line_segment;
	private JButton circle;
	private JButton ellipse;
	private JButton triangle;
	private JButton rectangle;
	private JButton square;
	private JButton orange;
	private JButton pink;
	private JButton magenta;
	private JButton col;
	private JButton red;
	private JButton green;
	private JButton blue;
	private JButton white;
	private JButton black;
	private JButton grey;
	private JButton yellow;
	private JButton cyan;
	private JButton save;
	private JButton load;
	private JButton redo;
	private JButton undo;
	private JButton plugin_import;
	private JRadioButton radio_update;
	private JRadioButton radio_remove;
	private JCheckBox check_fill;
	private JCheckBox check_out;
	private Paint paint;
	private JTextArea text;
	private VectorDraw engine;
	private List<Class <? extends Shape> > list_shapes;
	private Shape new_shape;
	private Shape prev_shape;
	private Color curr_out_color;
	private Color curr_fill_color;
	private boolean created_shape = false;
	private boolean is_update = false;
	private boolean is_remove = false;
	private boolean is_update_foreign = false;
	private int index;
	
	@SuppressWarnings("rawtypes")
	public Gui(){
		
		super("Paint");
		setLayout(null);
		setSize(1100, 700);
		setResizable(false);
		factory = new ShapeFactory();
		
		getContentPane().setBackground(Color.BLACK);
		curr_out_color = Color.BLACK;
		curr_fill_color = null;
		
		paint = new Paint();
		paint.setBounds(135, 0, this.getWidth(), this.getHeight());
		add(paint);
		
		engine = new VectorDraw();
		engine.addObserver(paint);
		
		choose_shape = new JComboBox<String>();
		list_shapes = new ArrayList<Class <? extends Shape> >();
		try{
			List<Class <? extends Shape> > tmp_list = engine.getSupportedShapes();
			for(int i = 0;i < tmp_list.size();i++){
				list_shapes.add(tmp_list.get(i));
			}
			choose_shape.removeAllItems();
			choose_shape.addItem("Choose one");
			for(Class c : list_shapes){
				choose_shape.addItem(c.getSimpleName());
			}
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "can not load plugin");
		}
		
		add_shapes();
		add_more();
		add_colors();
		
		undo = new JButton("Undo");
		undo.setForeground(Color.RED);
		undo.setFont(new Font("Roman", Font.BOLD, 12));
		undo.setBackground(Color.GRAY);
		undo.setBounds(5, 515, 63, 30);
		undo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				is_update = false;
				is_remove = false;
				created_shape = false;
				select.setText("Click a shape");
				select.setEnabled(true);
				try{
					engine.undo();
				}
				catch(Exception e){
					JOptionPane.showMessageDialog(null, e.getMessage());
				//	e.printStackTrace();
				}
				
			}
		});
		add(undo);
		
		redo = new JButton("Rdo");
		redo.setForeground(Color.BLUE);
		redo.setFont(new Font("Roman", Font.BOLD, 12));
		redo.setBackground(Color.GRAY);
		redo.setBounds(73, 515, 58, 30);
		redo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				is_update = false;
				is_remove = false;
				created_shape = false;
				select.setText("Click a shape");
				select.setEnabled(true);
				try{
					engine.redo();
				}
				catch(Exception e){
					JOptionPane.showMessageDialog(null, e.getMessage());
				//	e.printStackTrace();
				}
				
			}
		});
		add(redo);
		
		save = new JButton("Save");
		save.setForeground(Color.BLUE);
		save.setFont(new Font("Roman", Font.BOLD, 12));
		save.setBackground(Color.LIGHT_GRAY);
		save.setBounds(5, 555, 63, 50);
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				is_update = false;
				is_remove = false;
				created_shape = false;
				select.setText("Click a shape");
				select.setEnabled(true);
				String path = JOptionPane.showInputDialog(null, "Enter saving path", "Information", JOptionPane.PLAIN_MESSAGE);
				
				if(path == null)return;
				try{
					engine.save(path);
				}
				catch(Exception e){
					JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.PLAIN_MESSAGE);
				}
				
			}
		});
		add(save);
		
		load = new JButton("Load");
		load.setForeground(Color.BLUE);
		load.setFont(new Font("Roman", Font.BOLD, 12));
		load.setBackground(Color.LIGHT_GRAY);
		load.setBounds(5, 610, 63, 55);
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				is_update = false;
				is_remove = false;
				created_shape = false;
				select.setText("Click a shape");
				select.setEnabled(true);
				String path = JOptionPane.showInputDialog(null, "Enter loading path", "Information", JOptionPane.PLAIN_MESSAGE);
				
				if(path == null)return;
				try{
					engine.load(path);
				}
				catch(Exception e){
					JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.PLAIN_MESSAGE);
				}
				
			}
		});
		add(load);
		
		plugin_import = new JButton("plgn");
		plugin_import.setForeground(Color.BLACK);
		plugin_import.setFont(new Font("Roman", Font.LAYOUT_LEFT_TO_RIGHT, 10));
		plugin_import.setBackground(Color.WHITE);
		plugin_import.setBounds(73, 555, 57, 110);
		plugin_import.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				is_update = false;
				is_remove = false;
				created_shape = false;
				select.setText("Click a shape");
				select.setEnabled(true);
				try{
					List<Class <? extends Shape> > tmp_list = engine.getSupportedShapes();
					list_shapes.clear();
					for(int i = 0;i < tmp_list.size();i++){
						list_shapes.add(tmp_list.get(i));
					}
					choose_shape.removeAllItems();
					choose_shape.addItem("Choose one");
					for(Class c : list_shapes){
						choose_shape.addItem(c.getSimpleName());
					}
				}catch(Exception e){
					JOptionPane.showMessageDialog(null, "can not load plugin");
				}
			}
		});
		add(plugin_import);
		
		text = new JTextArea();
		text.setBounds(140, 628, 100, 50);
		text.setLineWrap(true);
		text.setFont(new Font("Roman", Font.BOLD, 18));
		text.setForeground(Color.BLACK);
		text.setBackground(Color.WHITE);
		add(text);
		
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void add_shapes(){
		
		JPanel shape_group = new JPanel();
		shape_group.setLayout(null);
		shape_group.setBorder(BorderFactory.createTitledBorder(null, "Shapes",2, 2, new Font("Roman", Font.BOLD, 15), Color.BLACK));
		shape_group.setBackground(Color.GRAY);
		shape_group.setBounds(10, 10, 120, 210);
		add(shape_group);
		
		
		
		line_segment = new JButton("Ln");
		line_segment.setFont(new Font("Roman", Font.LAYOUT_LEFT_TO_RIGHT, 11));
		line_segment.setBounds(10, 30, 50, 40);
		line_segment.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					is_update = false;
					is_remove = false;
					select.setText("Click a shape");
					select.setEnabled(true);
					new_shape = factory.createNewInstance(Line.class.getName());
					created_shape = true;
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error loading line");
				}
				
			}
		});
		shape_group.add(line_segment);
		
		circle = new JButton("Cr");
		circle.setFont(new Font("Roman", Font.LAYOUT_LEFT_TO_RIGHT, 11));
		circle.setBounds(65, 30, 45, 40);
		circle.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					is_update = false;
					is_remove = false;
					select.setText("Click a shape");
					select.setEnabled(true);
					new_shape = factory.createNewInstance(Circle.class.getName());
					created_shape = true;					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error loading circle");
				}
				
			}
		});
		shape_group.add(circle);
		
		ellipse = new JButton("ell");
		ellipse.setBounds(10, 80, 50, 40);
		ellipse.setFont(new Font("Roman", Font.LAYOUT_LEFT_TO_RIGHT, 11));
		ellipse.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					is_update = false;
					is_remove = false;
					select.setText("Click a shape");
					select.setEnabled(true);
					new_shape = factory.createNewInstance(Ellipse.class.getName());
					created_shape = true;					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error loading ellipse");
				}
				
			}
		});
		shape_group.add(ellipse);
		
		triangle = new JButton("tri");
		triangle.setBounds(65, 80, 45, 40);
		triangle.setFont(new Font("Roman", Font.LAYOUT_LEFT_TO_RIGHT, 11));
		triangle.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					is_update = false;
					is_remove = false;
					select.setText("Click a shape");
					select.setEnabled(true);
					new_shape = factory.createNewInstance(Triangle.class.getName());
					created_shape = true;					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error loading triangle");
				}
				
			}
		});
		shape_group.add(triangle);
		
		rectangle = new JButton("rct");
		rectangle.setBounds(10, 130, 50, 40);
		rectangle.setFont(new Font("Roman", Font.LAYOUT_LEFT_TO_RIGHT, 11));
		rectangle.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					is_update = false;
					is_remove = false;
					select.setText("Click a shape");
					select.setEnabled(true);
					new_shape = factory.createNewInstance(Rectangle.class.getName());
					created_shape = true;					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error loading rectangle");
				}
				
			}
		});
		shape_group.add(rectangle);
		
		square = new JButton("sqr");
		square.setBounds(65, 130, 47, 40);
		square.setFont(new Font("Rman", Font.LAYOUT_LEFT_TO_RIGHT, 10));
		square.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					is_update = false;
					is_remove = false;
					select.setText("Click a shape");
					select.setEnabled(true);
					new_shape = factory.createNewInstance(Square.class.getName());
					created_shape = true;					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error loading square");
				}
				
			}
		});
		shape_group.add(square);
		
		choose_shape.setBounds(10, 180, 100, 25);
		choose_shape.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				
				if(event.getStateChange() != ItemEvent.SELECTED)return;
				is_update = false;
				is_remove = false;
				created_shape = false;
				select.setText("Click a shape");
				select.setEnabled(true);
				int index = choose_shape.getSelectedIndex();
				if(index == 0)return;
				try {
					new_shape = list_shapes.get(index-1).newInstance();
					new_shape.setPosition(new Point(0,0));
					add_foreign_shape();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error loading "+ index + choose_shape.getItemAt(index));
				}
				
				
			}
		});
		shape_group.add(choose_shape);
		
	}
	
	private void add_colors(){
		
		JPanel color_groups = new JPanel();
		curr_fill_color = Color.GREEN;
		curr_out_color = Color.BLACK;
		color_groups.setLayout(null);
		color_groups.setBorder(BorderFactory.createTitledBorder(null, "Colors", 1, 2, null, Color.BLUE));
		color_groups.setBackground(Color.WHITE);
		color_groups.setBounds(10, 230, 115, 190);
		add(color_groups);
		
		
		red = new JButton();
		red.setBackground(Color.RED);
		red.setBounds(10, 20, 20, 20);
		red.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent event){
					if(check_fill.isSelected()){
						curr_fill_color = Color.RED;
						check_fill.setForeground(Color.RED);
					}
					if(check_out.isSelected()){
						curr_out_color = Color.RED;
						check_out.setForeground(Color.RED);
					}
					
				}
		});
		color_groups.add(red);
		
		green = new JButton();
		green.setBackground(Color.GREEN);
		green.setBounds(35, 20, 20, 20);
		green.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				if(check_fill.isSelected()){
					curr_fill_color = Color.GREEN;
					check_fill.setForeground(Color.GREEN);
				}
				if(check_out.isSelected()){
					curr_out_color = Color.GREEN;
					check_out.setForeground(Color.GREEN);
				}
			}
		});
		color_groups.add(green);
		
		blue = new JButton();
		blue.setBackground(Color.BLUE);
		blue.setBounds(60, 20, 20, 20);
		blue.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				if(check_fill.isSelected()){
					curr_fill_color = Color.BLUE;
					check_fill.setForeground(Color.BLUE);
				}
				if(check_out.isSelected()){
					curr_out_color = Color.BLUE;
					check_out.setForeground(Color.BLUE);
				}
			}
		});
		color_groups.add(blue);
		
		yellow = new JButton();
		yellow.setBackground(Color.YELLOW);
		yellow.setBounds(85, 20, 20, 20);
		yellow.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				if(check_fill.isSelected()){
					curr_fill_color = Color.YELLOW;
					check_fill.setForeground(Color.YELLOW);
				}
				if(check_out.isSelected()){
					curr_out_color = Color.YELLOW;
					check_out.setForeground(Color.YELLOW);
				}
			}
		});
		color_groups.add(yellow);
		
		cyan = new JButton();
		cyan.setBackground(Color.CYAN);
		cyan.setBounds(10, 45, 20, 20);
		cyan.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				if(check_fill.isSelected()){
					curr_fill_color = Color.CYAN;
					check_fill.setForeground(Color.CYAN);
				}
				if(check_out.isSelected()){
					curr_out_color = Color.CYAN;
					check_out.setForeground(Color.CYAN);
				}
			}
		});
		color_groups.add(cyan);
		
		grey = new JButton();
		grey.setBackground(Color.GRAY);
		grey.setBounds(35, 45, 20, 20);
		grey.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				if(check_fill.isSelected()){
					curr_fill_color = Color.GRAY;
					check_fill.setForeground(Color.GRAY);
				}
				if(check_out.isSelected()){
					curr_out_color = Color.GRAY;
					check_out.setForeground(Color.GRAY);
				}
			}
		});
		color_groups.add(grey);
		
		black = new JButton();
		black.setBackground(Color.BLACK);
		black.setBounds(60, 45, 20, 20);
		black.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				if(check_fill.isSelected()){
					curr_fill_color = Color.BLACK;
					check_fill.setForeground(Color.BLACK);
				}
				if(check_out.isSelected()){
					curr_out_color = Color.BLACK;
					check_out.setForeground(Color.BLACK);
				}
			}
		});
		color_groups.add(black);
		
		white = new JButton();
		white.setBackground(Color.WHITE);
		white.setBounds(85, 45, 20, 20);
		white.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				if(check_fill.isSelected()){
					curr_fill_color = Color.WHITE;
					check_fill.setForeground(Color.WHITE);
				}
				if(check_out.isSelected()){
					curr_out_color = Color.WHITE;
					check_out.setForeground(Color.WHITE);
				}
			}
		});
		color_groups.add(white);
		
		orange = new JButton();
		orange.setBackground(Color.ORANGE);
		orange.setBounds(85, 70, 20, 20);
		orange.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				if(check_fill.isSelected()){
					curr_fill_color = Color.ORANGE;
					check_fill.setForeground(Color.ORANGE);
				}
				if(check_out.isSelected()){
					curr_out_color = Color.ORANGE;
					check_out.setForeground(Color.ORANGE);
				}
			}
		});
		color_groups.add(orange);
		
		pink = new JButton();
		pink.setBackground(Color.PINK);
		pink.setBounds(10, 70, 20, 20);
		pink.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				if(check_fill.isSelected()){
					curr_fill_color = Color.PINK;
					check_fill.setForeground(Color.PINK);
				}
				if(check_out.isSelected()){
					curr_out_color = Color.PINK;
					check_out.setForeground(Color.PINK);
				}
			}
		});
		color_groups.add(pink);
		
		magenta = new JButton();
		magenta.setBackground(Color.MAGENTA);
		magenta.setBounds(35, 70, 20, 20);
		magenta.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				if(check_fill.isSelected()){
					curr_fill_color = Color.MAGENTA;
					check_fill.setForeground(Color.MAGENTA);
				}
				if(check_out.isSelected()){
					curr_out_color = Color.MAGENTA;
					check_out.setForeground(Color.MAGENTA);
				}
			}
		});
		color_groups.add(magenta);
		
		col = new JButton();
		col.setBackground(new Color(122, 23, 32));
		col.setBounds(60, 70, 20, 20);
		col.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				if(check_fill.isSelected()){
					curr_fill_color = new Color(122, 23, 32);
					check_fill.setForeground(new Color(122, 23, 32));
				}
				if(check_out.isSelected()){
					curr_out_color = new Color(122, 23, 32);
					check_out.setForeground(new Color(122, 23, 32));
				}
			}
		});
		color_groups.add(col);
		
		check_fill = new JCheckBox("Fill col");
		check_fill.setForeground(curr_fill_color);
		check_fill.setBackground(Color.WHITE);
		check_fill.setBounds(10, 95, 80, 20);
		color_groups.add(check_fill);
		
		check_out = new JCheckBox("Out col");
		check_out.setForeground(curr_out_color);
		check_out.setBackground(Color.WHITE);
		check_out.setBounds(10, 120, 80, 20);
		color_groups.add(check_out);
		
		choose_col = new JButton("choose");
		choose_col.setBounds(10, 150, 95, 30);
		choose_col.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Color color = JColorChooser.showDialog(null, "Choose color", null);
				if(check_fill.isSelected()){
					curr_fill_color = color;
					check_fill.setForeground(color);
				}
				if(check_out.isSelected()){
					curr_out_color = color;
					check_out.setForeground(color);
				}
			}
		});
		color_groups.add(choose_col);
		color_groups.repaint();
	}
	
	private void add_more(){
		
		JPanel change_prop = new JPanel();
		change_prop.setLayout(null);
		change_prop.setBackground(Color.DARK_GRAY);
		change_prop.setBounds(10, 430, 120, 80);
		change_prop.setBorder(BorderFactory.createTitledBorder(null, "UPD prop", 2, 2, null, Color.WHITE));
		add(change_prop);
		
		radio_update = new JRadioButton("Update");
		radio_update.setBounds(5, 15, 100, 15);
		radio_update.setBackground(Color.DARK_GRAY);
		radio_update.setForeground(Color.WHITE);
		radio_update.setSelected(true);
		radio_update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(radio_update.isSelected()){
					radio_remove.setSelected(false);
				}
				else{
					radio_update.setSelected(true);
				}
				
			}
		});
		change_prop.add(radio_update);
		
		radio_remove = new JRadioButton("Remove");
		radio_remove.setBounds(5, 30, 100, 15);
		radio_remove.setBackground(Color.DARK_GRAY);
		radio_remove.setForeground(Color.WHITE);
		radio_remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(radio_remove.isSelected()){
					radio_update.setSelected(false);
				}
				else{
					radio_remove.setSelected(true);
				}
				
			}
		});
		change_prop.add(radio_remove);
		
		select = new JButton("Click a shape");
		select.setBounds(5, 50, 110, 20);
		select.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				created_shape = false;
				new_shape = null;
				if(radio_update.isSelected()){
					select.setText("WAITING");
					select.setEnabled(false);
					is_update = true;
				}
				else if(radio_remove.isSelected()){
					select.setText("WAITING");
					select.setEnabled(false);
					is_remove = true;
				}
				
			}
		});
		change_prop.add(select);
		
	}
	
	
	private void add_foreign_shape(){
		
		try{
			new_shape.setColor(curr_out_color);
			new_shape.setFillColor(curr_fill_color);
			JFrame get_shape = new JFrame("Propertis of Shape : " + new_shape.getClass().getSimpleName());
			get_shape.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			get_shape.setSize(400, 90 + new_shape.getProperties().size() * 40 + 80);
			get_shape.setVisible(true);
			get_shape.setAlwaysOnTop(true);
			get_shape.setLayout(null);
			get_shape.setResizable(false);
			
			get_shape.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					new_shape = null;
					prev_shape = null;
				}
			});
			
			SetProp set_prop = new SetProp(get_shape){
				@Override
				public void actionPerformed(ActionEvent event) {
					this.setChanged();
					try{
						this.notifyObservers(new_shape.getProperties());
					}catch(Exception e){
						new_shape = null;
						prev_shape = null;
						JOptionPane.showMessageDialog(null, "Error loading : bad input");
						this.frame.dispose();
						return;
					}
					
					if(prev_shape == null){
						engine.addShape(new_shape);
					}
					else{
						engine.updateShape(prev_shape, new_shape);
					}
					new_shape = null;
					prev_shape = null;
					this.frame.dispose();
				}
			};
			
			JLabel pos_x1 = new JLabel("Position : x1");
			pos_x1.setBounds(30, 20, 200, 30);
			get_shape.add(pos_x1);
			@SuppressWarnings("serial")
			save_on_exit txt_pos_x1 = new save_on_exit(null){
				@Override
				public void update(Observable ob, Object properties) {
					Point point = new_shape.getPosition();
					point.x = (int) Double.parseDouble(this.getText());

					
					new_shape.setPosition(point);
				}
				
			};
			txt_pos_x1.setBounds(260, 20, 100, 30);
			set_prop.addObserver(txt_pos_x1);
			get_shape.add(txt_pos_x1);
			
			JLabel pos_y1 = new JLabel("Position : y1");
			pos_y1.setBounds(30, 20 + 40, 200, 30);
			get_shape.add(pos_y1);
			@SuppressWarnings("serial")
			save_on_exit txt_pos_y1 = new save_on_exit(null){
				@Override
				public void update(Observable ob, Object properties) {
					Point point = new_shape.getPosition();
					point.y = (int) Double.parseDouble(this.getText());

					new_shape.setPosition(point);
				}
				
			};
			txt_pos_y1.setBounds(260, 20 + 40, 100, 30);
			set_prop.addObserver(txt_pos_y1);
			get_shape.add(txt_pos_y1);
			
			int i = 2;
			for(String key : new_shape.getProperties().keySet()){

				JLabel lbl = new JLabel(key);
				lbl.setBounds(30, 20 + 40 * i, 200, 30);
				get_shape.add(lbl);
				@SuppressWarnings("serial")
				save_on_exit txt_prop = new save_on_exit(key){
					@Override
					public void update(Observable ob, Object properties) {
						@SuppressWarnings("unchecked")
						HashMap<String, Double> tmp = (HashMap<String, Double>) properties;
						tmp.put(key, Double.valueOf(Double.parseDouble(this.getText())));
						new_shape.setProperties(tmp);
					}
					
				};
				txt_prop.setBounds(260, 20 + 40 * i, 100, 30);
				set_prop.addObserver(txt_prop);
				get_shape.add(txt_prop);
				
				++i;
			}
			
			JButton ok = new JButton("ok");
			ok.setBounds(120, 20 + 40 * i, 100, 30);
			ok.addActionListener(set_prop);
			get_shape.add(ok);
			
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "Error loading "+ new_shape.getClass().getSimpleName());
		}
		
	}
	
	
	private class Paint extends JPanel implements Observer{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 6031974783565444128L;
		private ArrayList<Shape> shapes = new ArrayList<Shape>();
		Boolean prop_setted = false;
		
		public void paintComponent(Graphics g){
			
			super.paintComponent(g);
			setBackground(Color.WHITE);
			
			if(g instanceof Graphics2D){
				g = (Graphics2D) g;
				((Graphics2D) g).setStroke(new BasicStroke(8));
			}

			int i = 0;

			while(shapes != null && i < shapes.size()){
				Shape shape = shapes.get(i);
				if(prev_shape == null || !shape.getPosition().equals(prev_shape.getPosition())
						|| !shape.getProperties().equals(prev_shape.getProperties())
						|| !shape.getFillColor().equals(prev_shape.getFillColor())
						|| !shape.getColor().equals(prev_shape.getColor())){
					shape.draw(g);
				}
				else{
					new_shape.draw(g);
				}
				
				++i;
			}
			if(new_shape != null && !is_update){
				new_shape.draw(g);
			}
			
			addMouseListener(new MouseAdapter(){
				
					@Override
					public void mouseClicked(MouseEvent event){
						if(!is_update && !is_remove && !is_update_foreign)return;
						
						int x = event.getX(), y = event.getY();
						int min_dist = Integer.MAX_VALUE;
						int i = 0;
						Shape[] list_shapes = engine.getShapes();
						if(list_shapes == null){
							JOptionPane.showMessageDialog(null, "No shapes found");
							is_update = false;
							is_remove = false;
							is_update_foreign = false;
							select.setText("Click a shape");
							select.setEnabled(true);
							return;
						}
						
						for(Shape shape : list_shapes){
							Point p = shape.getPosition();
							int delta_x = p.x - x;
							int delta_y = p.y - y;
							int tmp_val = delta_x * delta_x + delta_y * delta_y;
							if(tmp_val < min_dist){
								min_dist = tmp_val;
								index = i;
							}
							++i;
						}
						if(is_remove){
							is_remove = false;
							engine.removeShape(list_shapes[index]);
							select.setText("Click a shape");
							select.setEnabled(true);
							return;
						}
						
						try {
							new_shape = (Shape) list_shapes[index].clone();
							prev_shape = (Shape) list_shapes[index].clone();
						} catch (CloneNotSupportedException e) {
							JOptionPane.showMessageDialog(null, "error", "Information", JOptionPane.PLAIN_MESSAGE);
							is_update = false;
							is_remove = false;
							is_update_foreign = false;
							select.setText("Click a shape");
							select.setEnabled(true);
							return;
						}
						boolean flag = false;
						for(int j = 0; j < 6; j++){
							if(new_shape.getClass().getSimpleName().equals(Gui.this.list_shapes.get(j).getSimpleName())){
								flag = true;
								break;
							}
						}
						if(!flag){
							try{
								add_foreign_shape();
							}catch(Exception e){
								JOptionPane.showMessageDialog(null, "error", "Information", JOptionPane.PLAIN_MESSAGE);
							}
							select.setText("Click a shape");
							select.setEnabled(true);
							is_update = false;
							return;
						}
						created_shape = true;
					}
					@Override
					public void mousePressed(MouseEvent event){
						if(created_shape == false)return;
						new_shape.setPosition(new Point(event.getX(), event.getY()));
						new_shape.setColor(curr_out_color);
						new_shape.setFillColor(curr_fill_color);
					}
					@Override
					public void mouseReleased(MouseEvent event){
						if(!created_shape)return;
						if(!prop_setted){
							prop_setted = false;
							created_shape = false;
							new_shape = null;
							return;
						}
						created_shape = false;
						
						if(!is_update)engine.addShape(new_shape);
						else{
							is_update = false;
							engine.updateShape(prev_shape, new_shape);
							prev_shape = null;
						}
						select.setText("Click a shape");
						select.setEnabled(true);
						new_shape = null;
					}
					
			});
			
			addMouseMotionListener(new MouseMotionListener(){

				@Override
				public void mouseDragged(MouseEvent event) {
					
					text.setText(String.format("X : %d\nY : %d", event.getX(), event.getY()));
					if(created_shape == false)return;
					prop_setted = true;
					repaint();
					int x = event.getX(), y = event.getY();
					String s = new_shape.getClass().getSimpleName();
					if(s.equals("Line")){
						new_shape.getProperties().put("x2", Double.valueOf(x));
						new_shape.getProperties().put("y2", Double.valueOf(y));
					}
					else if(s.equals("Circle")){
						x -= new_shape.getPosition().x;
						y -= new_shape.getPosition().y;
						new_shape.getProperties().put("Radius", Double.valueOf(Math.abs(x * 2)));
					}
					else if(s.equals("Square")){
						x -= new_shape.getPosition().x;
						new_shape.getProperties().put("SideLength", Double.valueOf(Math.abs(x * 2)));
					}
					else if(s.equals("Rectangle")){
						x -= new_shape.getPosition().x;
						y -= new_shape.getPosition().y;
						new_shape.getProperties().put("Width", Double.valueOf(Math.abs(x * 2)));
						new_shape.getProperties().put("Height", Double.valueOf(Math.abs(y * 2)));
					}
					else if(s.equals("Ellipse")){
						x -= new_shape.getPosition().x;
						y -= new_shape.getPosition().y;
						new_shape.getProperties().put("Width", Double.valueOf(Math.abs(2 * x)));
						new_shape.getProperties().put("Height", Double.valueOf(Math.abs(2 * y)));
					}
					else if(s.equals("Triangle")){
						new_shape.getProperties().put("x2", Double.valueOf(x));
						new_shape.getProperties().put("y2", Double.valueOf(y));
						new_shape.getProperties().put("x3", 2 * new_shape.getPosition().x - Double.valueOf(x));
						new_shape.getProperties().put("y3", Double.valueOf(y));
					}
					


				}

				@Override
				public void mouseMoved(MouseEvent event) {

					text.setText(String.format("X : %d\nY : %d", event.getX(), event.getY()));
				}
				
			});
			
			
		}

		@SuppressWarnings("unchecked")
		@Override
		public void update(Observable ooo, Object shapes) {
			this.shapes = (ArrayList<Shape>) shapes;
			this.repaint();
		}

	}
	
	private abstract class save_on_exit extends JTextField implements Observer{

		/**
		 * 
		 */
		private static final long serialVersionUID = 2979955210181063482L;
		
		protected String key;
		public save_on_exit(String key){
			this.key = key;
		}
		
	}
	
	private abstract class SetProp extends Observable implements ActionListener{
		
		protected JFrame frame;
		public SetProp(JFrame frame){
			this.frame = frame;
		}
	}


}
