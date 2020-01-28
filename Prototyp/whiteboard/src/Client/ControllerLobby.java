package Client;

import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ControllerLobby implements MouseListener, ActionListener, KeyListener, FocusListener, DocumentListener {

	private ViewLobby view;
	private ModelLobby lobby;
	private String room; // Zwischenspeicher --> Es wird sich der Name des ausgewaehlten Raumes gemerkt.
	private boolean errorTriggered;
	String passwordForRoom = "";
	private boolean hasJoined = false;

	public ControllerLobby(ViewLobby view, ModelLobby lobby) {
		this.view = view;
		this.lobby = lobby;

		this.lobby.searchRoom(""); // initialisierung der Raumliste nach nichts speziellem gesucht
		// Timer für autoupdate
		new Timer(1000, trigger -> { // update einmal die Sekunde
			view.update();
		}).start();
	}

	public void tableButtonEvent(String roomname) {
		Room roomToJoin = lobby.getRoomInList(roomname);
		room = roomname;
		PasswordPopup frame = new PasswordPopup(this, room, roomToJoin);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				view.setErrorField("");
				view.update();
			}
		});
		if (lobby.getRoomInList(roomname).getPasswordLocket()) {
			frame.setVisible(true);
		} else {
			tableButtonEvent(roomToJoin, roomname, frame);
		}
	}

	public void tableButtonEvent(Room roomToJoin, String roomname, PasswordPopup frame) {
		if (!roomToJoin.getPasswordLocket()) {
			try {
				lobby.joinRoom(roomname, "");
				roomToJoin.addUser();
				ViewRoom room = new ViewRoom(new ModelRoom(), roomToJoin);
				hasJoined = true;
			} catch (IllegalArgumentException e) {
				view.setErrorField(e.getMessage());
				this.errorTriggered = true;
			}
		} else {
			try {
				lobby.joinRoom(roomname, passwordForRoom);
				roomToJoin.addUser();
				ViewRoom room = new ViewRoom(new ModelRoom(), roomToJoin);
				hasJoined = true;
				view.setErrorField("");
			} catch (IllegalArgumentException e) {
				this.passwordForRoom = "";
				view.setErrorField(e.getMessage());
				this.errorTriggered = true;
			}
		}
		if (hasJoined) {
			hasJoined = false;
			passwordForRoom = "";
			frame.dispose();
		}
	}

	// Button oder andere Actionevents für die Lobby
	@Override
	public void actionPerformed(ActionEvent ev) {
		resetError();
		if (ev.getSource() instanceof JButton) {
			JButton pressed = (JButton) ev.getSource();
			if (pressed.getText().equals("Raum erstellen")) {

				String roomname = view.getRoomName().getText();
				if (!(null == view.getRoomPW().getPassword())) {
					passwordForRoom = new String(view.getRoomPW().getPassword());

				} else {
					passwordForRoom = "";
				}
				// Konstruktion der Raeume
				if (!roomname.equals("Raum Name...")) {
					try {
						lobby.createRoom(roomname, passwordForRoom);
						Room roomToCreate = lobby.getRoomInList(roomname);
						roomToCreate.addUser();
						ViewRoom room = new ViewRoom(new ModelRoom(), roomToCreate); // fix constructor
						lobby.searchRoom("");
						hasJoined = true;
					} catch (IllegalArgumentException e) {
						view.setErrorField(e.getMessage());
						this.errorTriggered = true;
					}
				}
			}
		}
		if (hasJoined) {
			hasJoined = false;
			passwordForRoom = "";
		}
	}

	// Key press events hier vor allem Enter für die Suche
	@Override
	public void keyPressed(KeyEvent e) {
		resetError();
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (e.getSource() == view.getSearchRoom()) {
				lobby.searchRoom(view.getSearchRoom().getText());
			}
			view.update();
		}
	}

	// Mouse events für die Lobby, sinnvoll vor allem für den room
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
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	// Es wurden dynamische Fehlermeldungen von der Schwesterngruppe gewuenscht je
	// nach Zustand in RoomName
	@Override
	public void keyTyped(KeyEvent e) {

	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getRoom() {
		return this.room;
	}

	@Override
	public void focusGained(FocusEvent e) {
		resetError();
		if (e.getSource() instanceof JTextField) {
			if (e.getSource() == view.getRoomName()) {
				JTextField temp = (JTextField) e.getSource();
				if (temp.getText().equals("Raum Name...")) {
					temp.setText("");
				}
			} else if (e.getSource() == view.getSearchRoom()) {
				JTextField temp = (JTextField) e.getSource();
				if (temp.getText().equals("Raum suchen..")) {
					temp.setText("");
				}
			} else if (e.getSource() == view.getRoomPW()) {
				JPasswordField temp = (JPasswordField) e.getSource();
				if (new String(temp.getPassword()).equals("123456")) {
					temp.setText("");
				}
			}
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		resetError();
		if (e.getSource() instanceof JTextField) {
			if (e.getSource() == view.getRoomName()) {
				JTextField temp = (JTextField) e.getSource();
				if (temp.getText().equals("")) {
					temp.setText("Raum Name...");
				}
			} else if (e.getSource() == view.getSearchRoom()) {
				JTextField temp = (JTextField) e.getSource();
				if (temp.getText().equals("")) {
					temp.setText("Raum suchen..");
				}
			}
		}
	}

	private void resetError() {
		if (errorTriggered) {
			view.setErrorField("");
			this.errorTriggered = false;
		}
	}

	public boolean isHasJoined() {
		return this.hasJoined;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		if (e.getDocument().getLength() < 4) {
			view.setErrorField("Der Name muss aus mindestens 4 Buchstaben bestehen.");
		} else if (e.getDocument().getLength() > 25) {
			view.setErrorField("Der Name darf aus maximal 25 Zeichen bestehen.");
		} else if (view.getRoomName().getText().startsWith(" ") || view.getRoomName().getText().endsWith(" ")) {
			view.setErrorField("Der Name darf nicht mit einem Leerzeichen anfangen oder aufhören.");
		} else {
			view.setErrorField("");
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		changedUpdate(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		changedUpdate(e);
	}
}
