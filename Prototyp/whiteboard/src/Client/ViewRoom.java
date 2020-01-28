package Client;

import util.Drawing;
import util.PointOnLine;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewRoom {

	private final int buttonScale = 50;
	private static final long serialVersionUID = 1L;
	private int windowSizeX=600;
	private int windowSizeY=800;
	
	public boolean drawMode;
	public boolean eraseMode;
	public Room room;
	private Graphics2D g2d;
	
	private ArrayList<PointOnLine> points;
	private ArrayList<Drawing> drawings;
	public ArrayList<PointOnLine> pointsPerDrawing;
	private ArrayList<PointOnLine> pointsPerErasing;
	private PointOnLine currentPoint;
	private JTable userTable;
	private JTextField chatField;
	private ControllerRoom conRoom;
	public JFrame paint;
	
	//Buttons, Panels, etc
	private JButton btnPencil;
	private JButton btnColorPicker;
	private JButton btnEraser;
	private JButton btnSelector;
	private JButton btnSnapshot;
	private JButton btnHome;
	private JButton btnDeleteWhiteboard;
	private JButton btnExport;
	private JButton btnUndo;
	private JButton btnRedo;
	private JToolBar toolbarLeft; 
	private JToolBar toolbarNorth;
	private JTextArea chatWindow;
	private JPanel chatPanel;

	public void update() {}

	public ViewRoom(ModelRoom modell, Room room) {
        this.room = room;
		this.conRoom = new ControllerRoom(this, modell, room);

		//Swing-GUI Elements
		this.paint = new JFrame();
		this.toolbarLeft = new JToolBar();
		this.toolbarNorth = new JToolBar();
		this.btnPencil = new JButton();
		this.btnColorPicker = new JButton();
		btnColorPicker.setName("btnColorPicker");
		btnColorPicker.addActionListener(conRoom);
		this.btnHome = new JButton();
		btnHome.setName("btnHome");
		btnHome.addActionListener(conRoom);

		this.btnSnapshot = new JButton();
		btnSnapshot.setName("btnSnapshot");
		btnSnapshot.addActionListener(conRoom);

		this.btnEraser = new JButton();
		this.btnSelector = new JButton();
		this.btnDeleteWhiteboard = new JButton();
		this.btnExport = new JButton();
		this.btnUndo = new JButton();
		this.btnRedo = new JButton();
		this.userTable = new JTable();
		this.chatPanel = new JPanel();
		this.chatField = new JTextField();
		this.chatWindow = new JTextArea();
		
		//Logic Elements
		this.drawMode = false;
		this.eraseMode = false;
		this.currentPoint = null;
		this.drawings = new ArrayList<>();
		this.pointsPerDrawing = new ArrayList<>();
		this.pointsPerErasing = new ArrayList<>();

		//Container Settings
		paint.setTitle("Willkommen in Raum " + room.getName());
		paint.setSize(windowSizeX, windowSizeY);
		toolbarLeft.setOrientation(JToolBar.VERTICAL);
		chatPanel.setLayout(new BorderLayout(0, 0));
		chatWindow.setEditable(false);
		chatWindow.setRows(10);
		 
		paint.addWindowListener(conRoom);
		
		 btnPencil.addMouseListener(new MouseAdapter() {
			 
			 //If drawing is complete, save it as a drawElement
			  @Override public void mouseClicked(MouseEvent arg0) {
				  drawMode = !drawMode;
				  eraseMode = false;
				  if(drawMode) {
						PointOnLine[] pointsDrawing= new PointOnLine[pointsPerDrawing.size()];
						for(int i=0; i<pointsPerDrawing.size(); i++){
							pointsDrawing[i]=pointsPerDrawing.get(i);
						}
						if(pointsDrawing.length != 0) {
							drawings.add(new Drawing(modell.getColor(), 20, pointsDrawing, "THISUSER")); //ToDO if implementing more, adapit this
						}
							pointsPerDrawing.clear();
				  }
			  }
		 });

		//Add Toolbar Elements
		toolbarLeft.add(btnPencil);
		toolbarLeft.add(btnEraser);
		toolbarLeft.add(btnSelector);
		toolbarLeft.add(btnColorPicker);
		toolbarLeft.add(btnSnapshot);

		toolbarNorth.add(btnHome);
		toolbarNorth.add(btnDeleteWhiteboard);
		toolbarNorth.add(btnExport);
		toolbarNorth.add(btnUndo);
		toolbarNorth.add(btnRedo);

		//Adding Elements to root paint Frame
		paint.getContentPane().add(toolbarLeft, BorderLayout.WEST);
		paint.getContentPane().add(toolbarNorth, BorderLayout.NORTH);
		
		paint.getContentPane().add(new JComponent() {//Not quite sure how to outsource this
			private List<PointOnLine> points = new ArrayList<PointOnLine>();
			private List<PointOnLine> erasepoints = new ArrayList<PointOnLine>();
			private PointOnLine currentPoint = null;
			{
				MouseAdapter mouseAdapter = new MouseAdapter() {
					private void addPoint(Point p){
						if (drawMode) {
							currentPoint = new PointOnLine(p.x, p.y, modell.getColor());
							points.add(currentPoint);
							pointsPerDrawing.add(currentPoint);
							repaint();
						}
					}
					public void mousePressed(MouseEvent e) {
						addPoint(e.getPoint());
					}
					public void mouseDragged(MouseEvent e) {
						addPoint(e.getPoint());
					}
					public void mouseReleased(MouseEvent e) {
						addPoint(e.getPoint());
						if (drawMode) {
							points.add(null);
							pointsPerDrawing.add(null);
						}
					}
				};
				addMouseListener(mouseAdapter);
				addMouseMotionListener(mouseAdapter);
			}

			@Override
			protected void paintComponent(Graphics g) {

				if(drawMode && !points.isEmpty()) {
					Graphics2D g2d = (Graphics2D) g.create();
					g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
					g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
					g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

					PointOnLine first = null;
					for (PointOnLine point : points) {
						if(null != first && null != point) {
							g2d.setColor(point.getColor());
							g2d.drawLine(first.getX(), first.getY(), point.getX(), point.getY());
						}
						first = point;
					}
					g2d.dispose();
				}
			}
		});
		
		paint.getContentPane().add(userTable, BorderLayout.EAST);
		paint.getContentPane().add(chatPanel, BorderLayout.SOUTH);

		chatPanel.add(chatWindow);

		//Adding Elements to chatPanel
		chatPanel.add(chatField, BorderLayout.SOUTH);
		
		setIcons();
		//Set JFrame to visible
		paint.setVisible(true);
	}
	
	public void setIcons() {
		try {

		    Image pencil = ImageIO.read(getClass().getResource("/icons/pencil-striped-symbol-for-interface-edit-buttons.png")).getScaledInstance(buttonScale, buttonScale, buttonScale);
		    Image eraser = ImageIO.read(getClass().getResource("/icons/eraser.png")).getScaledInstance(buttonScale, buttonScale, buttonScale);
		    Image home = ImageIO.read(getClass().getResource("/icons/home-icon.png")).getScaledInstance(buttonScale, buttonScale, buttonScale);
		    Image save = ImageIO.read(getClass().getResource("/icons/Save-icon.png")).getScaledInstance(buttonScale, buttonScale, buttonScale);
		    Image trash = ImageIO.read(getClass().getResource("/icons/trash-icon.png")).getScaledInstance(buttonScale, buttonScale, buttonScale);
		    Image undo = ImageIO.read(getClass().getResource("/icons/Arrows-Undo-icon.png")).getScaledInstance(buttonScale, buttonScale, buttonScale);
		    Image redo = ImageIO.read(getClass().getResource("/icons/Arrows-Redo-icon.png")).getScaledInstance(buttonScale, buttonScale, buttonScale);
		    Image mark = ImageIO.read(getClass().getResource("/icons/Cursor-Select-icon.png")).getScaledInstance(buttonScale, buttonScale, buttonScale);
		    Image colorPicker = ImageIO.read(getClass().getResource("/icons/Editing-Paint-Palette-icon.png")).getScaledInstance(buttonScale, buttonScale, buttonScale);
		    Image snapshot = ImageIO.read(getClass().getResource("/icons/Editing-Screenshot-icon.png")).getScaledInstance(buttonScale, buttonScale, buttonScale);

		    btnPencil.setIcon(new ImageIcon(pencil));
		    btnColorPicker.setIcon(new ImageIcon(colorPicker));
		    btnEraser.setIcon(new ImageIcon(eraser));
		    btnHome.setIcon(new ImageIcon(home));
		    btnExport.setIcon(new ImageIcon(save));
		    btnDeleteWhiteboard.setIcon(new ImageIcon(trash));
		    btnUndo.setIcon(new ImageIcon(undo));
		    btnRedo.setIcon(new ImageIcon(redo));
		    btnSelector.setIcon(new ImageIcon(mark));
		    btnSnapshot.setIcon(new ImageIcon(snapshot));

		  } catch (IOException ex) {
		    ex.printStackTrace();
		  }		
	}
}