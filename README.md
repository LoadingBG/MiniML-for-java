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
### IDs
IDs are introduced in v1.1. IDs can be used for aesier access to a given node:
```
root
  'root id'
  child
    // IDs with "'" are ok, everyting between the first and the last ' is a part of the ID
    '''
    =value
  __end__
__end__
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
