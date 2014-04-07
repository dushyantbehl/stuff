public class SuffixTree {

	private final Character magicCharacter = '$';
	private Node root;
	private String text;
	private int nodeId = 0;
	
	private Node current_node;
	private int current_edge;
	private int current_length;
	
	private int remainder = 0;
	private int textLength = 0;
		
	public static class Index {
		public Node node;
		public int pos;
	}
	
	public SuffixTree(){
		root = new Node(nodeId++);
		text = null;
	}
	
	public Node getRoot(){
		return root;
	}
	
	public Character getMagicCharacter(){
		return magicCharacter;
	}
	
	public boolean searchPos(Node node, int phaseID){
		if(current_length >= node.length(phaseID)){
			current_edge += node.length(phaseID);
			current_length -= node.length(phaseID);
			current_node = node;
			return true;
		}
		return false;
	}
		
	public void createTree(String in){
		text = in;
		if(text == null || text.length()==0){
			return;
		}
		else if( (text.charAt( text.length()-1 ) != magicCharacter) ){
			text = text + magicCharacter;
		}
	
		textLength = text.length();
		System.out.println("Test length is "+textLength);
		System.out.println("Test is "+text);
		
		Node temp;
		current_node = root;
		current_edge = 0;
		current_length = 0;
						
		/* Run n phases of the algorithm with phase 'i' having 'i' extensions.*/
		for( int i=0; i<text.length(); i++){
			/* Started phase i+1. */
			remainder++;
			temp = null;
			
			while(remainder > 0){
			
				if(current_length == 0) {
					current_edge = i;
				}
				if( !current_node.children.containsKey(text.charAt(current_edge)) ){
					Node leaf = new Node(nodeId++);
					leaf.setEdgeStart(i);
					leaf.setEdgeEnd(textLength);
					current_node.children.put(text.charAt(current_edge), leaf);
					leaf.setParent(current_node);
					if(temp != null){
						temp.suffixLink = current_node;
					}
					temp = current_node;
				}
				else{
					Node next = current_node.children.get(text.charAt(current_edge));
					if(current_length >= next.length(i)){
						current_edge += next.length(i);
						current_length -= next.length(i);
						current_node = next;
						//System.out.println("walking down with cur edge "+current_edge+" cur len "+current_length);						
						continue;
					}
					if( text.charAt(next.getEdgeStart() + current_length) == text.charAt(i)){
						current_length++;
						if(temp != null){
							temp.suffixLink = current_node;
						}
						temp = current_node;
						break;
					}
					/*Split the nodes here*/
					Node split = new Node(nodeId++);
					split.setEdgeStart(next.getEdgeStart());
					split.setEdgeEnd(next.getEdgeStart()+current_length);
					current_node.children.put(text.charAt(current_edge), split);
					split.setParent(current_node);
					
					Node leaf = new Node(nodeId++);
					leaf.setEdgeStart(i);
					leaf.setEdgeEnd(textLength);
					split.children.put(text.charAt(i), leaf);
					leaf.setParent(split);
					
					//System.out.println("next edge start is "+next.getEdgeStart());
					//System.out.println("current edge is "+current_edge);
				
					next.setEdgeStart(next.getEdgeStart()+current_length);
					split.children.put( text.charAt(next.getEdgeStart()), next);
					next.setParent(split);
					
					if(temp != null){
						temp.suffixLink = split;
					}
					temp = split;
				}
				remainder--;
			
				if(current_node.isRoot() && current_length > 0){
					current_length--;
					current_edge = i - remainder + 1;
				} else {
					if(current_node.suffixLink != null){
						current_node = current_node.suffixLink;
					} else{
						current_node = root;
					}
				}
			}
		}
	}
	
	String edgeString(Node node) {
        return text.substring(node.getEdgeStart(), node.getEdgeEnd());
    }

    void printTree() {
        System.out.println("digraph {");
        System.out.println("\trankdir = LR;");
        System.out.println("\tedge [arrowsize=0.4,fontsize=10]");
        System.out.println("\tnode1 [label=\"\",style=filled,fillcolor=lightgrey,shape=circle,width=.1,height=.1];");
        System.out.println("//------leaves------");
        printLeaves(root);
        System.out.println("//------internal nodes------");
        printInternalNodes(root);
        System.out.println("//------edges------");
        printEdges(root);
        System.out.println("//------suffix links------");
        printSLinks(root);
        System.out.println("}");
    }

    void printLeaves(Node x) {
        if (x.children.size() == 0)
        	System.out.println("\tnode"+x.getNodeId()+" [label=\"\",shape=point]");
        else {
            for (Node child : x.children.values())
                printLeaves(child);
        }
    }

    void printInternalNodes(Node x) {
        if (!x.isRoot() && x.children.size() > 0)
        	System.out.println("\tnode"+x.getNodeId()+" [label=\"\",style=filled,fillcolor=lightgrey,shape=circle,width=.07,height=.07]");

        for (Node child : x.children.values())
            printInternalNodes(child);
    }

    void printEdges(Node x) {
        for (Node child : x.children.values()) {
        	System.out.println("\tnode"+x.getNodeId()+" -> node"+child.getNodeId()+" [label=\""+edgeString(child)+"\",weight=3]");
            printEdges(child);
        }
    }

    void printSLinks(Node x) {
        if (x.suffixLink != null)
        	System.out.println("\tnode"+x.getNodeId()+" -> node"+x.suffixLink.getNodeId()+" [label=\"\",weight=1,style=dotted]");
        for (Node child : x.children.values())
            printSLinks(child);
    }
}