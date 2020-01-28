package test;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JToolBar;
public class DrawArea extends JFrame {
	private boolean drawMode = true;
	private boolean trackMouse = false;
	Graphics2D g2d;
	private ArrayList<Point> points;
	private Point currentPoint = null;
	public DrawArea() {
		/*
		 * points = new ArrayList<>();
		 * 
		 * Canvas canvas = new Canvas();
		 * 
		 * 
		 * canvas.addMouseMotionListener(new MouseMotionAdapter() {
		 * 
		 * @Override public void mouseMoved(MouseEvent mousePos) { //if(trackMouse) {
		 * //System.out.println("Mouse moved"); //g2d.drawOval(mousePos.getX(),
		 * mousePos.getY(), 5, 5); currentPoint = new Point(mousePos.getX(),
		 * mousePos.getY()); points.add(currentPoint); //System.out.println("test");
		 * repaint();
		 * 
		 * //} } });
		 * 
		 * 
		 * canvas.addMouseListener(new MouseAdapter() {
		 * 
		 * 
		 * @Override public void mousePressed(MouseEvent e) {
		 * 
		 * }
		 * 
		 * @Override public void mouseReleased(MouseEvent e) {
		 * System.out.println("mouse released"); trackMouse = false; } });
		 * 
		 * 
		 * 
		 * canvas.setBackground(Color.WHITE); getContentPane().add(canvas,
		 * BorderLayout.CENTER);
		 * 
		 * JButton btnTest = new JButton("test"); btnTest.addMouseListener(new
		 * MouseAdapter() {
		 * 
		 * @Override public void mouseClicked(MouseEvent arg0) { drawMode = !drawMode; }
		 * }); getContentPane().add(btnTest, BorderLayout.WEST);
		 * 
		 * 
		 * //RENDERING STUFF JFrame frame = new JFrame();
		 * 
		 * frame.add(getContentPane()); frame.add(new DrawPanel()); frame.setSize(800,
		 * 600); frame.setVisible(true); }
		 * 
		 * 
		 * public void paintComponent(Graphics g) { g2d = (Graphics2D) g; g2d.setPaint (
		 * Color.BLACK ); System.out.println("test"); for ( Point point : points ) {
		 * //g2d.drawOval(point.x, point.y, 20, 20); g2d.fillOval(point.x, point.y, 20,
		 * 20); //g2d.drawLine(point.x, point.y, point.x, point.y);
		 * 
		 * }
		 * 
		 * 
		 */
		//boolean drawMode = false;
		JFrame paint = new JFrame();
		
		
		JToolBar toolbar = new JToolBar();
		toolbar.setOrientation(JToolBar.VERTICAL);
		 
		
		JButton btnPencil = new JButton("Pencil"); btnPencil.addMouseListener(new MouseAdapter() {
			 
			  @Override public void mouseClicked(MouseEvent arg0) { drawMode = !drawMode; }
			  });
		
		JButton btnOtherStuff = new JButton("OtherStuff");
		toolbar.add(btnPencil);
		toolbar.add(btnOtherStuff);
			  
		
		paint.add(toolbar, BorderLayout.WEST);
		paint.add(new JComponent() {
			private List<Point> points = new ArrayList<Point>();
			private Point currentPoint = null;
			{
				MouseAdapter mouseAdapter = new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
					}
					public void mouseDragged(MouseEvent e) {
						if (drawMode) {
							currentPoint = new Point(e.getX(), e.getY());
							System.out.println("x: " + e.getX());
							System.out.println("y: " + e.getY());
							points.add(currentPoint);
							repaint();
						}
					}
					public void mouseReleased(MouseEvent e) {
					}
				};
				addMouseListener(mouseAdapter);
				addMouseMotionListener(mouseAdapter);
			}
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setPaint(Color.BLACK);
				for (Point point : points) {
					g2d.fillOval(point.x, point.y, 20, 20);
				}
			}
		});
		
		paint.setSize(500, 500);
		// paint.setLocationRelativeTo ( null );
		paint.setVisible(true);
	}
}