package Client;

import util.Element;

import javax.swing.*;
import java.awt.event.*;

public class ControllerRoom implements ActionListener, MouseListener, KeyListener, WindowListener {
	
	private ViewRoom view;
	private ModelRoom room;
	private Room specificRoom;

	public ControllerRoom(ViewRoom view, ModelRoom room, Room specificRoom) {
		this.room = room;
		this.view = view;
		this.specificRoom = specificRoom;
		
		// Time für autoupdate - Intervall?
		new Timer(1000, trigger -> {			                                                                    // update einmal die Sekunde?
			view.update();
		}).start();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource() instanceof JButton) {
			JButton pressed = (JButton) ev.getSource();

			if(pressed.getName().equals("btnColorPicker")) {
				ColorPopup cp = new ColorPopup(this, room);
				cp.setVisible(true);
			} else if(pressed.getName().equals("btnHome")){
				view.room.removeUser();
				view.paint.dispose();
			} else if(pressed.getName().equals("btnSnapshot")) {
				room.saveBoard(view.paint);
			}

			// TODO Auto-generated method stub
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	public void transferDrawings(Element element){
		room.addDrawingElement(element);
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        specificRoom.removeUser();
    }

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}