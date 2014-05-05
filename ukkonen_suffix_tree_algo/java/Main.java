import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map.Entry;

public class Main {

	/*Prints the output specified in the assignment statement.*/
	public static void printOutput(PrintWriter pw, Node node, SuffixTree t) throws Exception{
		pw.println(node.getNodeId()+":");
		for (Entry<Character, Node> entry : node.getChildren().entrySet()) {
			pw.println(entry.getKey()+" "+entry.getValue().getEdgeStart()+
								 " "+entry.getValue().getEdgeEnd()+
								 " "+entry.getValue().getNodeId()+";");
		}
		pw.println("");
		for (Entry<Character, Node> entry : node.getChildren().entrySet()) {
			printOutput(pw, entry.getValue(),t);
		}
		return;
	}
	
	public static void main(String[] args) throws Exception {
		
		BufferedReader br = null;
		String header = null;
		String input = null;
		long startTime = 0;
		long endTime = 0;
		long timeTaken = 0;
	
		/* Allocate a new suffix tree object. */
		SuffixTree ukkonen = new SuffixTree();
		
		/*Exit if no input file is specified to the program.*/
		if(args.length < 2){
			System.out.println("usage: java Main <input-file> <output-file>");
			System.exit(1);
		}
		String inputFile = args[0];
		String outputFile = args[1];
		
		System.out.println("Using the input file \""+inputFile+"\"");
		
		/* Input string should be stored in the file in single line. 
		   For the test case we assume that the input string can be 
		   read from the file in one go.*/
				
		try{
			br = new BufferedReader(new FileReader(inputFile));
			header = br.readLine();
            System.out.println("Header is "+header);
            input = br.readLine();
            System.out.println("Input of length "+input.length());
			br.close();
		}catch (IOException e) {
			System.err.println("ERROR: Exception occured while reading input file "+e.getMessage());
			System.exit(1);
		}
		System.out.println("Read the input from the file of length "+input.length());
		System.out.println("Using the magic character \'"+ukkonen.getMagicCharacter()+"\'");
				
		startTime = System.currentTimeMillis();
		ukkonen.createTree(input.substring(0,10));
		endTime = System.currentTimeMillis();
		
		timeTaken = endTime - startTime;
		System.out.println("The time taken to construct the suffix tree is "+timeTaken+" milliseconds");
		
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(outputFile));
			PrintWriter pw = new PrintWriter(bw);
			/* Print output to the file here. */
			printOutput(pw, ukkonen.getRoot(),ukkonen);	
			pw.close();
			bw.close();
		} catch (IOException e) {
			System.err.println("ERROR: Exception occured while writing output file "+e.getMessage());
			System.exit(1);
		}
				
		/* Cleanup memory before exiting. */
		ukkonen.free();		
		System.out.println("Output Saved to the file \""+outputFile+"\"");
		System.exit(0);
	}
}
