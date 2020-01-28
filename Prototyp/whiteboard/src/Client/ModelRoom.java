package Client;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import util.Element;
import util.RoomAbtr;

import javax.imageio.ImageIO;
import javax.swing.*;


public class ModelRoom extends RoomAbtr {

	private boolean hasName;
	private boolean chatStaus;
	private boolean userInfoStatus;
	private Color color;

	private List<Color> colors = Arrays.asList(
			new Color(224,211,4),
			new Color(232, 183, 14),
			new Color(221, 135, 5),
			new Color(221, 132, 30),
			new Color(215, 93, 32),
			new Color(219, 36, 35),
			new Color(181, 7, 117),
			new Color(102, 56, 130),
			new Color(65, 74, 142),
			new Color(42, 106, 163),
			new Color(9, 139, 173),
			new Color(4, 132, 86),
			new Color(130, 173, 39),
			new Color(0,0,0),
			new Color(122, 122, 122),
			new Color(255, 255, 255));

	private int thickness;

	private List<String> users = Arrays.asList("Bob", "Rob", "Robert");

	public enum role{};

	private int selectedTool;

	private List<String> verlaufChat;
	
	private int countMessages;

	private Color contextcolor;

	private int contextThickness;

	private Element selectedElement;

	public void leaveRoom() {}

	public void choosename(String username) throws Exception {
		if(username.length() < 4) {
			throw new Exception("Ein Name muss aus mindestens 4 Buchstaben bestehen.");
		} else {
			if(username.length() > 25) {
				throw new Exception("Ein Name darf aus maximal 25 Zeichen bestehen.");
			} else {
				if(username.startsWith(" ") || username.endsWith(" ")) {
					throw new Exception("Ein Name darf aus maximal 25 Zeichen bestehen.");
				} else {
					for (int i = 0; i < users.size(); i++)
						if (users.get(i).equals(username)) throw new Exception("Der Name muss eindeutig sein. Es gibt bereits einen User mit diesem Namen.");
					users.add(username);
				}
			}
		}
	}


	public void saveBoard(JFrame paint) {

		BufferedImage image = new BufferedImage(paint.getWidth(), paint.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		paint.paint(g);
		try{
			ImageIO.write(image, "png", new File("WhiteboardSnap.png"));
		} catch (IOException ex){}
	}

	public void writeInChat(String text, String nickname) {
		this.verlaufChat.add(nickname + ": " + text + "\n");
	}

	public void selectTool(int tool) {
		this.selectedTool = tool;
	}

	public void selectColor(Color color) {
		this.color = color;
	}

	public Color getColor(){
		return this.color;
	}

	public List<Color> getColors(){
		return this.colors;
	}

	public void selectThickness(int thickness) {
		this.thickness = thickness;
	}

	public void updateElements(LinkedList<Element> elements) {}

	public ModelRoom() {}

	public void selectElement(Element element) {
		this.selectedElement = element;
	}

	public boolean isHasName() {
		return hasName;
	}

	public void setHasName(boolean hasName) {
		this.hasName = hasName;
	}
}