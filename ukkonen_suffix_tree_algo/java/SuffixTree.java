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
	private int nodeId = 1;
	
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

		/* Check if the input is okay. */
		if(in == null || in.length()==0){
			return;
		}
		/* Add the magic char if required. We build only explicit suffix tree. */
		else if( (in.charAt( in.length()-1 ) != magicCharacter) ){
			in = in + magicCharacter;
		}
		text = in;
		textLength = text.length();
										
		/* Run n phases of the algorithm. */
		for( int i=0; i<text.length(); i++){
	
			times++;
			pos.setFrom(null);
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
						pos.setEdge( pos.getEdge() + current.length(i) );
						pos.setIndex( pos.getIndex() - current.length(i) );
						pos.setNode(current);
						
						/* Before we jump into next iteration we should set 
						   times to times+1 As the next iteration will first 
						   try to set times to times -1 but we don't want it to change */
						times++; 
						continue;
					}
					if( text.charAt(current.getEdgeStart() + pos.getIndex()) == text.charAt(i)){
						pos.setIndex(pos.getIndex()+1);
						if(pos.isFrom())
							pos.getFrom().setSuffixLink(pos.getNode());
						pos.setFrom(pos.getNode());
						break;
					}
					/* Split the node here. */
					Node sp = split(current, text.charAt(pos.getEdge()), pos.getIndex(), i);
					if(pos.isFrom())
						pos.getFrom().setSuffixLink(sp);
					pos.setFrom(sp);
				}
				
				/* If the edge label we want to add is not present in the current node then
				   create a new leaf and add it to the the newly created node. */
				else{
					addLeaf(pos.getNode(),text.charAt(pos.getEdge()),i);
					if(pos.isFrom())
						pos.getFrom().setSuffixLink(pos.getNode());
					pos.setFrom(pos.getNode());
				}
			
				if(pos.getNode().isRoot() && pos.getIndex() > 0){
					/* If we are inserting at only root ten just update the references and do nothing else. */
					pos.setIndex(pos.getIndex()-1);
					pos.setEdge(i-times+2);
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
}
