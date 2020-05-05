package miniml;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node in a MiniML document. 
 * 
 * <p>A node is a container, containing values and other nodes.</p>
 * @see Document
 */
public class Node {
	/**
	 * The document this node is in.
	 * @see Document
	 */
	private Document parentDoc;
	/**
	 * This node's name.
	 */
	private String name;
	/**
	 * This node's parent.
	 */
	private Node parent;
	/**
	 * This node's children. 
	 * Not private because {@code Document#parseDocument()} 
	 * has to access it directly to remove 
	 * unnecessary methods.
	 */
	private final List<Node> children;
	/**
	 * This node's values.
	 */
	final List<String> values;
	/**
	 * This node's ID, can be null.
	 */
	private String id;
	
	/**
	 * Creates the node.
	 * 
	 * @param name This node's name.
	 * @param parent This node's parent.
	 * @param parentDocument The document this node is in.
	 * @param update Whether or not to update the document.
	 * 	@see Document#update()
	 */
	Node(String name, Node parent, Document parentDocument, boolean update) {
		this.name = name;
		this.parent = parent;
		parentDoc = parentDocument;
		
		children = new ArrayList<>();
		values = new ArrayList<>();
		
		// Add this node as it's parent child
		if (this.parent != null) {
			this.parent.children.add(this);
			if (update) {
				parentDoc.update();
			}
		}
	}
	
	
	
	/**
	 * Adds a new value to this node and updates the document.
	 * 
	 * @param newValue The new value.
	 */
	public void addValue(String newValue) {
		values.add(newValue);
		parentDoc.update();
	}
	
	/**
	 * Removes the specified value from this node. 
	 * If the value is not a value of this node, 
	 * an {@code IllegalArgumentException} will be thrown.
	 * Otherwise, the document this node is in will be updated.
	 * 
	 * @param value The value to be removed.
	 */
	public void removeValue(String value) {
		if (values.remove(value)) {
			parentDoc.update();
		} else {
			throw new IllegalArgumentException("The value \"" + value + "\" was not found.");
		}
	}
	
	/**
	 * Removes the specified child from this node. 
	 * If the node is not a child of this node, 
	 * an {@code IllegalArgumentException} will be thrown. 
	 * Otherwise, the document this node is in will be updated.
	 * 
	 * @param child The child to be removed.
	 */
	public void removeChild(Node child) {
		if (children.remove(child)) {
			parentDoc.update();
		} else {
			throw new IllegalArgumentException("The node with name \"" + child.name + "\" was not found.");
		}
	}
	
	/**
	 * Removes all children of this node 
	 * with the specified name.
	 * 
	 * @param name The name of the children to be removed.
	 */
	public void removeChildrenByName(String name) {
		children.removeIf(child -> child.name.contentEquals(name));
		parentDoc.update();
	}
	
	
	
	/**
	 * Returns this node's name.
	 * 
	 * @return This node's name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns this node's parent.
	 * 
	 * @return This node's parent.
	 */
	public Node getParent() {
		return parent;
	}
	
	/**
	 * Returns this node's children.
	 * 
	 * @return This node's children.
	 */
	public List<Node> getChildren() {
		// Create a new object for security reasons
		return new ArrayList<>(children);
	}
	
	/**
	 * Returns this node's id.
	 * 
	 * @return This node's id.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Returns all children of this node 
	 * with the same name as the given one.
	 * 
	 * @param name The name of the children.
	 * @return All children with the given name.
	 */
	public List<Node> getChildrenByName(String name) {
		List<Node> sameName = new ArrayList<>();
		children.forEach(child -> {
			if (child.name.contentEquals(name)) {
				sameName.add(child);
			}
		});
		return sameName;
	}
	
	/**
	 * Returns this node's values.
	 * 
	 * @return This node's values.
	 */
	public List<String> getValues() {
		// Create a new object for security reasons
		return new ArrayList<>(values);
	}
	
	
	
	/**
	 * Represents this node as a string. The string representation is 
	 * the name of the node followed by the values and finally the 
	 * children. At the end the node is closed.
	 * @see Document#NODE_END
	 * 
	 * @param indent The indent of this node. Used for 
	 * aesthetic reasons only.
	 * 
	 * @return The string representation of this node.
	 */
	String asString(int indent) {
		final String lineSep = System.getProperty("line.separator");
		
		StringBuilder res = new StringBuilder();
		
		// Append name
		res.append("\t".repeat(indent))
			.append(name)
			.append(lineSep);
		
		// Append ID
		res.append("\t".repeat(indent + 1))
			.append('\'')
			.append(id)
			.append('\'')
			.append(lineSep);
		
		// Append values
		values.forEach(value -> res.append("\t".repeat(indent + 1))
				.append(Document.VALUE_PREFIX)
				.append(value)
				.append(lineSep));
		
		// Append children
		children.forEach(child -> res.append(child.asString(indent + 1)));
		
		// Append end
		res.append("\t".repeat(indent)).append(Document.NODE_END).append(lineSep);
		
		return res.toString();
	}
	
	/**
	 * Checks if a child has the given ID.
	 * 
	 * @param id The ID to check.
	 * @return The node having the ID wanted.
	 */
	Node checkForId(String id) {
		if (this.id.contentEquals(id)) {
			return this;
		}
		
		for (Node child : children) {
			Node result = child.checkForId(id);
			if (result != null) {
				return result;
			}
		}
		
		return null;
	}
	
	/**
	 * Sets the ID without updating the parent document.
	 * 
	 * @param newId The new ID.
	 */
	void setIdNoUpdate(String newId) {
		id = newId;
	}
	/**
	 * Sets the ID of the node.
	 * 
	 * @param newId The new ID.
	 */
	public void setId(String newId) {
		id = newId;
		parentDoc.update();
	}
}
