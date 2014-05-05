import java.util.HashMap;

/* Class to represent a node. */
public class Node {

	/* The parent node, for root parent is null. */
	private Node parent;
	
	/* The suffix link where this node points to.*/
	private Node suffixLink;
	
	/* The id of this node, kept for the sake of 
	   problem output specification, otherwise not required.*/
	private int nodeId;
	
	/* 
	 * The hash map storing all the child edges of this node,
	 * Key is the character with which the edge is labelled and
	 * Value is the node itself because we store edge in the node.
	 */
	private HashMap<Character , Node> children = null;
	
	/* We Store the incoming edges of the nodes in the nodes itself.
	   Starting and ending index of the edge label incoming to this node. */
	private int edgeStart;
	private int edgeEnd;
	
	/*Constructor takes node id as input. */
	public Node(int id){
		children = new HashMap<Character, Node>();
		parent = null;
		edgeStart = 0;
		nodeId = id;
		suffixLink = null;
	}

	/* Getter and Setters for private variables. */
	public int getEdgeStart() {
		return edgeStart;
	}

	public void setEdgeStart(int edgeStart) {
		this.edgeStart = edgeStart;
	}

	public int getEdgeEnd() {
		return edgeEnd;
	}

	public void setEdgeEnd(int edgeEnd) {
		this.edgeEnd = edgeEnd;
	}
		
	public HashMap<Character, Node> getChildren() {
		return children;
	}

	public void setChildren(HashMap<Character, Node> children) {
		this.children = children;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public long getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public Node getSuffixLink() {
		return suffixLink;
	}

	public void setSuffixLink(Node suffixLink) {
		this.suffixLink = suffixLink;
	}

	/* Returns the length of the edge incoming to this node.*/
	public int length(int phaseID){
		return Math.min(edgeEnd, phaseID) - edgeStart;
	}
	
	/* Node is leaf only if its children are null */
	public boolean isLeaf(){
		return (children.size() == 0);
	}
	
	/* Node is root only if parent is null. */
	public boolean isRoot(){
		return (this.parent == null);
	}
}
