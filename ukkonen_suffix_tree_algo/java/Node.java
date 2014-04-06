
import java.util.HashMap;

public class Node {

	private Node parent;
	public Node suffixLink;
	private final int inf = Integer.MAX_VALUE;
	public HashMap<Character , Node> children = null;
	
	/* We Store the incoming edges of the nodes in the nodes itself.
	   Starting and ending index of the edge label incoming to this node. */
	public int edgeStart;
	public int edgeEnd;
	
	public Node(){
		setChildren(new HashMap<Character, Node>());
		setParent(null);
		edgeStart = 0;
		edgeEnd = inf;
	}

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
	
	public boolean isLeaf(){
		return (children.size() == 0);
	}
	
	public boolean isRoot(){
		return (this.parent == null);
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
}
