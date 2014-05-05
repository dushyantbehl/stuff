/* Class to store the position. 
   Here position is represented by a node,
   the edge label inside the node and also
   index into the edge label.
 */
public class Index {
	
	//variables declaration.
	private Node node;
	private int edge;		
	private int index;
	private int phaseID;
    
	//Constructors
	public Index() {
		node = null;
		edge = 0;
		index = 0;
    }
	
	public Index (Node n) {
		node = n;
		edge = 0;
		index = 0;
	}

	public Index (Node n, int e, int p) {
		node = n;
		edge = e;
		index = p;
	}
	
	/* Getter and setters of all the private variables. */
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int pos) {
		this.index = pos;
	}
	
	public int getEdge() {
		return edge;
	}
	
	public void setEdge(int edge) {
		this.edge = edge;
	}
	
	public Node getNode() {
		return node;
	}
	
	public void setNode(Node node) {
		this.node = node;
	}

	public int getPhaseID() {
		return phaseID;
	}

	public void setPhaseID(int phaseID) {
		this.phaseID = phaseID;
	}
}
