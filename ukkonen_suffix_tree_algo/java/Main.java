
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.sun.org.apache.xml.internal.utils.StopParseException;

public class Main {

	/*Prints the output specified in the assignment statement.*/
	public static void printOutput(BufferedWriter bw, Node node) throws Exception{
		bw.write(node.getNodeId()+":");
		String out = null;
		for (Entry<Character, Node> entry : node.children.entrySet()) {
			out = entry.getKey()+" "+entry.getValue().getEdgeStart()+
								 " "+entry.getValue().getEdgeEnd()+
								 " "+entry.getValue().getNodeId()+";";
			bw.write(out);
		}
		bw.newLine();
		for (Entry<Character, Node> entry : node.children.entrySet()) {
			printOutput(bw, entry.getValue());
		}
		return;
	}
	
	public static void main(String[] args) throws Exception {
		
		BufferedReader br = null;
		String in = null;
		String input = "";
		long startTime = 0;
		long endTime = 0;
		long timeTaken = 0;
	
		SuffixTree ukkonen = new SuffixTree();
		
		/*Exit if no input file is specified to the program.*/
		if(args.length == 0){
			System.out.println("usage: java Main <input-file>");
			System.exit(1);
		}
		String inputFile = args[0];
		String outputFile = "SuffixTree.output";
		
		System.out.println("Using the input file \""+inputFile+"\"");
		
		/* Input string should be stored in the file in single line. 
		   For the test case we assume that the input string can be 
		   read from the file in one go.*/
		
		System.out.println("Reading input from the file");
		
		try{
			br = new BufferedReader(new FileReader(inputFile));

			while( (in = br.readLine()) != null){
				
				input += in;
			}
			br.close();
		}catch (IOException e) {
			System.err.println("ERROR: Exception occured while reading input file "+e.getMessage());
			System.exit(1);
		}
		System.out.println("Read the input from the file");
		System.out.println("Using the magic character \'"+ukkonen.getMagicCharacter()+"\'");
				
		startTime = System.currentTimeMillis();
		
		ukkonen.createTree(input);
		
		endTime = System.currentTimeMillis();
		
		timeTaken = endTime - startTime;
		System.out.println("The time taken to construct the suffix tree is "+timeTaken+" milliseconds");
		
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(outputFile));
			/* Print output to the file here. */
			printOutput(bw, ukkonen.getRoot());	
			bw.close();
		} catch (IOException e) {
			System.err.println("ERROR: Exception occured while writing output file "+e.getMessage());
			System.exit(1);
		}
		
		ukkonen.printTree();
		
		System.out.println("Output Saved to the file \""+outputFile+"\"");
		System.exit(0);
	}
}
