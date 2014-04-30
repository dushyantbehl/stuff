import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.Map.Entry;

/* The class to build the suffix tree. */
public class SuffixTree {

	/*The character used to convert implicit 
	  suffix tree to explicit suffix tree.  */
	private Character magicCharacter = '$';
	
	/*The root node of the tree.*/
	private Node root;
	
	/*The backing text string of the tree.*/
	private String text;
	
	/* The node id variable, used to assign unique
	   node id's to the nodes. */
	private int nodeId = 0;
	
	/* The variable storing where each addition should happen. */
	private Index pos;
	
	/* The variable which stores how many iterations to run. */
	private int times;
	
	/* Length of the backing string. */
	private int textLength;
			
	//Constructor
	public SuffixTree(){
		root = new Node(nodeId++);
		text = null;
		pos = new Index(root);
		times = 0;
		textLength = 0;
	}
	/* Getter and setters of some variables. */
	public Node getRoot(){
		return root;
	}
	
	public String getText(){
		return text;
	}
	
	public Character getMagicCharacter(){
		return magicCharacter;
	}
	
	public void setMagicCharacter( Character c ){
		magicCharacter = c;
	}
	
	/* Function to free the memory used by the tree. */
	public void free(){
		text = null;
		pos = null;
		freeNode(root);
	}
	
	/* Internal function used to free nodes recursively. */
	private void freeNode(Node node){
		for (Entry<Character, Node> entry : node.getChildren().entrySet()) {
			freeNode(entry.getValue());
		}
		node.setChildren(null);
		node = null;
	}
	
	/* Add a leaf to the node cur at the edge edge. */
	private void addLeaf (Node cur, Character edge, int phase){
		
		//Create a new node and add it to the internal node as a leaf.
		Node leaf = new Node(nodeId++);
		leaf.setEdgeEnd(textLength);
		leaf.setEdgeStart(phase);
		cur.getChildren().put(edge, leaf);
		leaf.setParent(cur);
	}
	
	/* Split the edge incoming to node cur and add a leaf node.*/
	private Node split (Node cur, Character edge, int len, int phase){
		
		//First create a new internal node with incoming edge
		//of the current node and edge ending after length len.
		Node split = new Node(nodeId++);
		split.setEdgeStart(cur.getEdgeStart());
		split.setEdgeEnd(cur.getEdgeStart()+len);
		pos.getNode().getChildren().put(edge, split);
		split.setParent(pos.getNode());
	
		//Add the current node to this newly created node
		//at the edge labelled by the character current label + len.
		cur.setEdgeStart(cur.getEdgeStart()+len);
		split.getChildren().put( text.charAt(cur.getEdgeStart()), cur);
		cur.setParent(split);
		
		//Now add a leaf to this node for the new character.
		addLeaf(split, text.charAt(phase), phase);
		return split;
	}
	
	/* Function to create the suffix tree using the input string "in".*/
	public void createTree(String in){
		Node from;
		text = in;
		
		/* Check if the input is okay. */
		if(text == null || text.length()==0){
			return;
		}
		/* Add the magic char if required. We build only explicit suffix tree. */
		else if( (text.charAt( text.length()-1 ) != magicCharacter) ){
			System.out.println("Adding Magic Character ");
			text = text + magicCharacter;
		}
		textLength = text.length();
		
		System.out.println("Suffix Tree: Text of length "+text.length());
										
		/* Run n phases of the algorithm with phase 'i' having 'i' extensions.*/
		for( int i=0; i<text.length(); i++){
			//System.out.println("Running phase "+i);
			times++;
			from = null;
			
			for(;times>0; times--){
				if(pos.getIndex() == 0)
					pos.setEdge(i);
				
				/*If the edge labelled with the character to be added is present in the current node
				  then search for the position where we want to add this substring and if the edge label
				  is already present then we do nothing, else we split the node into two and add the 
				  label to the newly created internal node.*/
				if( pos.getNode().getChildren().containsKey(text.charAt(pos.getEdge())) == true ){
					
					/* Fetch the node which is labelled with the character we want to add. */
					Node current = pos.getNode().getChildren().get(text.charAt(pos.getEdge()));
					
					/*If the string we want to add is already present then do nothing. */
					if(pos.getIndex() >= current.length(i)){
						pos.setIndex( pos.getEdge() + current.length(i) );
						pos.setIndex( pos.getIndex() - current.length(i) );
						pos.setNode(current);
						continue;
					}
					if( text.charAt(current.getEdgeStart() + pos.getIndex()) == text.charAt(i)){
						pos.setIndex(pos.getIndex()+1);
						if(from != null)
							from.setSuffixLink(pos.getNode());
						from = pos.getNode();
						break;
					}
					/* Split the node here. */
					Node sp = split(current, text.charAt(pos.getEdge()), pos.getIndex(), i);
					if(from != null)
						from.setSuffixLink(sp);
					from = sp;
				}
				/* If the edge label we want to add is not present in the current node then
				   create a new leaf and add it to the the newly created node. */
				else{
					addLeaf(pos.getNode(),text.charAt(pos.getEdge()),i);
					if(from != null)
						from.setSuffixLink(pos.getNode());
					from = pos.getNode();
				}
			
				if(pos.getNode().isRoot() && pos.getIndex() > 0){
					/* If we are inserting at only root ten just update the references and do nothing else. */
					pos.setIndex(pos.getIndex()-1);
					pos.setEdge(i-times);
				}
				else {
					/* Move the current node position to the suffix link of the current node.
					   If the node does not have the suffix link pointing to anyone then point
					   the current node to root, because we'll have to start from the root anyway.*/
					if(pos.getNode().getSuffixLink() != null){
						pos.setNode(pos.getNode().getSuffixLink());
					} else{
						pos.setNode(root);
					}
				}
			}
		}
	}
	
		
		String edgeString(Node node) {
	         return text.substring(node.getEdgeStart(), node.getEdgeEnd());
	     }
	 
	     void printTree( BufferedWriter bw ) throws java.io.IOException {
	    	 PrintWriter pw = new PrintWriter(bw);
	    	 pw.println("digraph {");
	    	 pw.println("\trankdir = LR;");
	    	 pw.println("\tedge [arrowsize=0.4,fontsize=10]");
	    	 pw.println("\tnode1 [label=\"\",style=filled,fillcolor=lightgrey,shape=circle,width=.1,height=.1];");
	    	 pw.println("//------leaves------");
	         printLeaves(root,pw);
	         pw.println("//------internal nodes------");
	         printInternalNodes(root,pw);
	         pw.println("//------edges------");
	         printEdges(root,pw);
	         pw.println("//------suffix links------");
	         printSLinks(root,pw);
	         pw.println("}");
	      }
	 
	     void printLeaves(Node x, PrintWriter pw) throws java.io.IOException {
	         if (x.getChildren().size() == 0)
	        	 pw.println("\tnode"+x.getNodeId()+" [label=\"\",shape=point]");
	         else {
	             for (Node child : x.getChildren().values())
	                 printLeaves(child,pw);
	         }
	    }
	 
	     void printInternalNodes(Node x, PrintWriter pw) throws java.io.IOException {
	         if (!x.isRoot() && x.getChildren().size() > 0)
	        	 pw.println("\tnode"+x.getNodeId()+" [label=\"\",style=filled,fillcolor=lightgrey,shape=circle,width=.07,height=.07]");
	 
	         for (Node child : x.getChildren().values())
	             printInternalNodes(child,pw);
	     }
	 
	     void printEdges(Node x, PrintWriter pw) throws java.io.IOException {
	         for (Node child : x.getChildren().values())  {
	        	 pw.println("\tnode"+x.getNodeId()+" -> node"+child.getNodeId()+" [label=\""+edgeString(child)+"\",weight=3]");
	             printEdges(child,pw);
	         }
	     }
	 
	     void printSLinks(Node x, PrintWriter pw) throws java.io.IOException {
	         if (x.getSuffixLink() != null)
	        	 pw.println("\tnode"+x.getNodeId()+" -> node"+x.getSuffixLink().getNodeId()+" [label=\"\",weight=1,style=dotted]");
	         for (Node child : x.getChildren().values())
	             printSLinks(child,pw);
	     }
}