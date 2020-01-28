package util;


import java.util.LinkedList;
import java.util.List;

/**
 * Klasse RoomAbtr
 * @author Arne Bruhns
 */
public abstract class RoomAbtr {
	
	private List<Element> elements = new LinkedList<>(); 		//Liste nur in abstrakter Klasse

	public void deleteElement(Element element) {

	}

	public void addImageElement(Element element) {

	}

	public void addTextElement(Element element) {

	}

	public void addDrawingElement(Element element) {
		elements.add(element);
	}

	public void redoAction() {

	}

	public void undoAction(String name) {

	}

	public void changeTextElementText(Element element) {

	}

	public void banUser(String name) {

	}

	public void setRole(String name, String role) {

	}

	public void transformElement(Element element, String operation, int x, int y, double factor) {

	}

}
