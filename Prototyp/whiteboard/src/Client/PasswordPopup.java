package Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasswordPopup extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtFrDiesenRaum;
	private JPasswordField passwordField;


	public PasswordPopup(ControllerLobby controllerLobby, String room, Room roomToJoin) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Passwort fuer " + room);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		
		txtFrDiesenRaum = new JTextField();
		txtFrDiesenRaum.setText("Fuer " + room + " wird ein Passwort benoetigt:");
		txtFrDiesenRaum.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(txtFrDiesenRaum);
		txtFrDiesenRaum.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setText("123456");
		contentPane.add(passwordField);
		
		JButton btnNewButton = new JButton("OK");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!(null == passwordField.getPassword())) {
					controllerLobby.passwordForRoom = new String(passwordField.getPassword());
					controllerLobby.tableButtonEvent(roomToJoin, room, PasswordPopup.this);
				}
				
			}
		});
		contentPane.add(btnNewButton);
	}
}
