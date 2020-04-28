# MiniML-for-java
MiniML (Mini Markup Language) is a simple markup language inspired by XML.

## Syntax
**For MiniML new lines are important!**<br/>
**MiniML documents have .mnml extension**<br/>
### Nodes
A MiniML document consists of nodes and values. Here is a simple document:
```
root
  child
    child of child
    __end__
  __end__
  child 2
    another child
    __end__
  __end__
__end__
```
```"__end__"``` is a marker for the last node's end. The name of the nodes can contain spaces. Just like XML, a MiniML document can have only one root.<br/>
### Values
Nodes can contain values:
```
root
  =root value
  child 1
    =value
    =another value
    empty child
    __end__
  child 2
    grandchild
      =value
    __end__
  __end__
__end__
```
Values start with ```=```. If a line starts with ```=```, it is considered a value of the previous node. Values outside of the root will be ignored:
```
=value outside of root // Ignored
root
  ... // Rest of document
__end__
=another value outside of root // Also ignored
```
### Comments
MiniML supports comments. Comments are all lines starting with ```//```. Inline comments are only supported after an ```"__end__"```.
```
// Comment
root
  // Another comment
  child 1 // This is not a commant, this is a part of the node's name
    =value // This is also not a comment, this is a part of the value.
  __end__
__end__ // This is a comment because "__end__" will end the "root" node and the rest of the line will be skipped
```

## Usage
### Initialization
```Java
File fileToMiniML = new File("path/to/doc.mnml"); // Has to be .mnml extension
Document minimlDoc = new Document(fileToMiniML);
```
or
```Java
String pathToMiniML = "path/to/doc.mnml";
Document minimlDoc = new Document(pathToMiniML);
```
### Method overview
Creation of new node:<br/>
```Java
Document#createNode(/*Name (String)*/, /*Parent (Node)*/) // Creates a new node and returns it (Node)
```
```Java
Document#getRoot() // Returns the root of the document (Node)
```
```Java
Node#getChildren() // Returns a copy of the node's children (List<Node>)
```
```Java
Node#getChildrenByName(/*Name (String*/) // Returns a copy of all children of the node with the name given (List<Node>)```<br/>
```Java
Node#getValues() // Returns a copy of the values of this node (List<String>)```<br/>
```Java
Node#addValue(/*New value (String)*/) // Adds a new value to the node (void)```<br/>
```Java
Node#removeValue(/*Value (String)*/) // Removes the given value (void)```<br/>
```Java
Node#removeChild(/*Child (Node)*/) // Removes the given child (void)```<br/>
```Java Node#removeChildrenByName(/*Name (String)*/) // Removes all children with the given name (void)```<br/>
