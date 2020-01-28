package Client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;


public class ViewLobby extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	private ModelLobby lobby;
	private ControllerLobby conLobby;
	private JPanel leftSide;
	private JPanel rightSide;
	private JTextField searchRoom;
	private JTextField roomName;
	private JPasswordField roomPW;
	private JTextArea errField;
	private JButton btnCreateRoom;
	private JTable roomList;
	private final String[] roomTableColumnNames = {"Raum Name"," Benutzerzahl", "Gesperrt", "Raum beitreten"};
	private DefaultTableModel model;


	public ViewLobby(ModelLobby lobby) {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.lobby = lobby;
		this.conLobby = new ControllerLobby(this, lobby);
		this.model = new DefaultTableModel(null, roomTableColumnNames);
		this.roomList = new JTable(model);
		

		getContentPane().setLayout(new BorderLayout(0, 0));
		leftSide = new JPanel();
		getContentPane().add(leftSide, BorderLayout.WEST);
		searchRoom = new JTextField();
		searchRoom.setText("Raum suchen..");
		searchRoom.setColumns(10);
		searchRoom.addFocusListener(conLobby);
		searchRoom.addKeyListener(conLobby);
		
		roomName = new JTextField();
		roomName.setText("Raum Name...");
		roomName.setColumns(10);
		roomName.addFocusListener(conLobby);
		roomName.addKeyListener(conLobby);
		roomName.getDocument().addDocumentListener(conLobby);
		
		roomPW = new JPasswordField("123456");
		roomPW.setToolTipText("Room Password");
		roomPW.addFocusListener(conLobby);
		
		errField = new JTextArea(11,11);
		errField.setEnabled(false);
		errField.setEditable(false);
		errField.setDisabledTextColor(Color.red);
		errField.setLineWrap(true);
		errField.setWrapStyleWord(true);
		
		btnCreateRoom = new JButton("Raum erstellen");
		btnCreateRoom.addActionListener(conLobby);
		
		GroupLayout gl_leftSide = new GroupLayout(leftSide);
		gl_leftSide.setHorizontalGroup(
			gl_leftSide.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_leftSide.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_leftSide.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnCreateRoom)
						.addComponent(roomPW, 186, 186, Short.MAX_VALUE)
						.addComponent(searchRoom, GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
						.addComponent(roomName)
						.addComponent(errField))
					.addContainerGap(174, Short.MAX_VALUE))
		);
		gl_leftSide.setVerticalGroup(
			gl_leftSide.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_leftSide.createSequentialGroup()
					.addContainerGap()
					.addComponent(searchRoom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(59)
					.addComponent(roomName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(roomPW, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(53)
					.addComponent(btnCreateRoom)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(errField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(41, Short.MAX_VALUE))
		);
		leftSide.setLayout(gl_leftSide);
		
		rightSide = new JPanel();
		getContentPane().add(rightSide, BorderLayout.CENTER);
		rightSide.setLayout(new GridLayout(1, 0, 0, 0));


		roomList.getColumn("Raum beitreten").setCellRenderer(new JButtonRenderer());
		roomList.setShowGrid(false);
		roomList.setFocusable(false);
		roomList.setRowSelectionAllowed(false);
		roomList.getTableHeader().setReorderingAllowed(false);	

		JScrollPane tableScroll = new JScrollPane(roomList);
		rightSide.add(tableScroll);

		errField.setBackground(leftSide.getBackground());
		//RENDERING STUFF
		JFrame frame = new JFrame();
		frame.setTitle("WhiteBoard - Willkommen in der Lobby.");
		frame.add(getContentPane());
		frame.setSize(800, 600);
		frame.pack();
		btnCreateRoom.requestFocusInWindow();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// Look and Feel wer moechte
		/*
		try {
			UIManager.setLookAndFeel( "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel" );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		SwingUtilities.updateComponentTreeUI(frame);
		 */
	}
	
	public void setErrorField(String errMsg) {
		errField.setText(errMsg);

	}

	public void update() {
		// Update der Roomlist
		int sizeOfRooms = lobby.getFilteredRooms().size();
		String[][] roomData = new String[sizeOfRooms][4];
		
		for (int i = 0; i < sizeOfRooms; i++) {
			Room temp = lobby.getFilteredRooms().get(i);
				roomData[i][0] = temp.getName();
				roomData[i][1] = temp.getCurrentUsers() + "/" + temp.getMaxUsers();
				if (temp.getPasswordLocket()) {
					roomData[i][2] = "locked";
				} else {
					roomData[i][2] = "";
				} 
				roomData[i][3] = roomData[i][0];
			
		}
		
		model.setDataVector(roomData, roomTableColumnNames); 
		roomList.getColumn("Raum beitreten").setCellRenderer(new JButtonRenderer());
		roomList.getColumn("Raum beitreten").setCellEditor(new JButtonEditor());
	}
	
	public void setController(ControllerLobby controller) {
		this.conLobby = controller;
	}

	public JTextField getSearchRoom() {
		return searchRoom;
	}

	public JTextField getRoomName() {
		return roomName;
	}

	public JPasswordField getRoomPW() {
		return this.roomPW;
	}

	public void setSearchRoom(JTextField searchRoom) {
		this.searchRoom = searchRoom;
	}

	public void setRoomName(JTextField roomName) {
		this.roomName = roomName;
	}

	public void setRoomPW(JPasswordField roomPW) {
		this.roomPW = roomPW;
	}	

	class JButtonRenderer implements TableCellRenderer {
		JButton button = new JButton();

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
				int col) {
			table.setShowGrid(false);
			table.setGridColor(Color.LIGHT_GRAY);
			table.setFocusable(false);
			table.setRowSelectionAllowed(false);
			button.setText("beitreten");
			String txt = (value == null) ? "" : value.toString();
			button.setName(txt);
			button.setToolTipText("join this room");
			//button.addActionListener(conLobby);
			return button;
		}
	}
	
	class JButtonEditor extends AbstractCellEditor implements TableCellEditor {
		private static final long serialVersionUID = 1L;
		JButton button;
		String txt;
		
		public JButtonEditor() {
			super();
			button = new JButton();
			button.setOpaque(true);
            //button.addActionListener(conLobby);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					conLobby.tableButtonEvent(button.getText());
				}
			});
		}

		@Override
		public Object getCellEditorValue() {
			return null;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
			
			txt = (value == null) ? "" : value.toString();
			button.setText(txt);
			return button;
		}
	}
}

