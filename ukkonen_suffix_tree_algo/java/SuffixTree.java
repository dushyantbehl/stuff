public class SuffixTree {

	private final Character magicCharacter = '$';
	private Node root;
	private String text;

	public static class Index {
		public Node node;
		public int pos;
	}
	
	public SuffixTree(){
		root = new Node();
		text = null;
	}
	
	public Node getRoot(){
		return root;
	}
	
	public Character getMagicCharacter(){
		return magicCharacter;
	}
	
	public Index searchPos(){
		Index toReturn = null;
		return toReturn;
	}
	
	public void createTree(String in){
		text = in;
		if(text.length()==0){
			return;
		}
		else if( (text.charAt( text.length()-1 ) != magicCharacter) ){
			text = text + magicCharacter;
		}
		Node cur = null;
		
		System.out.println(text);
		System.out.println("Length of the input is "+in.length());
		
		/* Run n phases of the algorithm with phase 'i' having 'i' extensions.*/
		for( int i=0; i<text.length(); i++){
			for (int j=0; j<=i; j++){
				System.out.println("Phase number "+i+" Extension "+j+" substring "+text.substring(j, i+1));
				cur = root;
				if(cur.isLeaf()){ 
					/*If it's a leaf extend the label.*/
					cur.edgeEnd = i;
				}
				else{
					/*If it's a node check if the last character 
					  is present or not. If the last character is 
					  present then do nothing else split the node
					  and update the labels to the node.*/
					Character tomatch = text.charAt(i);//substring(i-1, i);
					if(cur.children.containsKey(tomatch)){
						continue;
					}else{
						
					}
				}
			}
		}
	}
}