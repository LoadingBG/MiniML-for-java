package miniml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Document {
	/**
	 * The MiniML file extension.
	 */
	private static final String EXTENSION = "mnml";
	/**
	 * The node ender.
	 * @see Node
	 */
	static final String NODE_END = "__end__";
	/**
	 * The prefix for a value.
	 */
	static final String VALUE_PREFIX = "=";
	/**
	 * The prefix for a command.
	 */
	private static final String COMMENT_PREFIX = "//";
	
	/**
	 * The document's root.
	 */
	private Node root;
	/**
	 * The document's file
	 */
	private final File file;
	
	/**
	 * Creates a document from a file.
	 * 
	 * @param file The file of the MiniML file.
	 */
	public Document(File file) {
		// Null check
		if (file == null) {
			throw new NullPointerException("The file cannot be null.");
		}
		
		// Directory check
		if (!file.isFile()) {
			throw new IllegalArgumentException("The file has to be a file, not a directory.");
		}
		
		// Extension check
		String name = file.getName();
		String extension = name.substring(name.lastIndexOf('.') + 1, name.length());
		if (!extension.contentEquals(EXTENSION)) {
			throw new IllegalArgumentException("The file has to be ." + EXTENSION + " file");
		}
		
		// Existence check
		if (!file.exists()) {
			throw new IllegalArgumentException("The file does not exist.");
		}
		
		// "Cloning" the file for security reasons
		this.file = new File(file.getAbsolutePath());
		
		// Parse the document
		parseDocument();
	}
	
	/**
	 * Creates a document from the given path.
	 * 
	 * @param path The path to the MiniML file.
	 */
	public Document(String path) {
		this(new File(path));
	}
	
	
	/**
	 * Parses the file passed in the constructor. 
	 * 
	 * <p>Beware that parsing an empty file will be successful, 
	 * but the {@code root} of this document will be null.</p>
	 * 
	 * <p>Values outside the root node will be ignored.</p>
	 */
	private void parseDocument() {
		int lineNumber = 0;
		String line = "";
		
		try (Scanner sc = new Scanner(new FileInputStream(file))) {
			Stack<Node> nodeStack = new Stack<>();
			Set<String> ids = new HashSet<>();
			
			while (sc.hasNextLine()) {
				line = sc.nextLine().trim();
				++lineNumber;
				
				// Skip comments and values outside of the root node
				if (line.contentEquals("") 
						|| line.startsWith(COMMENT_PREFIX) 
						|| (line.startsWith(VALUE_PREFIX) && nodeStack.isEmpty())) {
					continue;
				}
				
				// Throw an exception for invalid document
				if (nodeStack.isEmpty() && root != null && !line.contentEquals(NODE_END)) {
					throw new IllegalStateException("A second root was found on line " + lineNumber + ".");
				}
				
				// End of node
				if (line.startsWith(NODE_END)) {
					nodeStack.pop();
				// Value
				} else if (line.startsWith(VALUE_PREFIX)) {
					nodeStack.peek().values.add(line.substring(VALUE_PREFIX.length()));
				// ID
				} else if (line.startsWith("'")) {
					String id = line.substring(1, line.lastIndexOf('\''));
					if (ids.contains(id)) {
						throw new RepeatingIdException("Repeating ID found on line " + lineNumber);
					}
					if (nodeStack.peek().getId() != null) {
						throw new RepeatingIdException("The node \"" + nodeStack.peek().getName() + "\" has more than one ID. Second Id found on line " + lineNumber);
					}
					nodeStack.peek().setId(id);
					ids.add(id);
				// Root
				} else if (nodeStack.isEmpty()) {
					root = new Node(line, null, this, false);
					nodeStack.push(root);
				// Node
				} else {
					nodeStack.push(new Node(line, nodeStack.peek(), this, false));
				}
			}
			
			throw new IllegalStateException("The node \"" + nodeStack.peek().getName() + "\" is not closed.");
		} catch (FileNotFoundException | NullPointerException e) {
			// Not possible, file existence is already checked
		} catch (EmptyStackException e) {
			// Node end whithout a node
			throw new IllegalStateException("A \"" + NODE_END + "\" without a matching node was found on line " + lineNumber + ".");
		}
	}
	
	
	
	/**
	 * Updates the document. This happens every time 
	 * the document tree is updated.
	 */
	void update() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(root.asString(0));
			writer.flush();
		} catch (IOException e) {
			throw new UpdateException("Something went wrong while updating the file.");
		}
	}
	
	/**
	 * Returns this document's root.
	 * 
	 * @return This document's root.
	 */
	public Node getRoot() {
		return root;
	}
	
	/**
	 * Creates a new node in this document.
	 * 
	 * @param name The name of the new node.
	 * @param parent The parent of the new node.
	 * @return The newly created node.
	 */
	public Node createNode(String name, Node parent) {
		return new Node(name, parent, this, true);
	}
	
	/**
	 * Returns the node which has the provided ID 
	 * or null if there is no node with the ID.
	 * 
	 * @param id The ID to search.
	 * @return The node with whe provided ID.
	 */
	public Node getNodeById(String id) {
		return root.checkForId(id);
	}
}
