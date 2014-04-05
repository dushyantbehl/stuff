
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

	public static void printOutput(String file, SuffixTree tree){
		BufferedWriter br = null;
		try {
			br = new BufferedWriter(new FileWriter(file));
			/* Print output to the file here. */
			
			br.close();
		} catch (IOException e) {
			System.err.println("ERROR: Exception occured while writing output file "+e.getMessage());
			System.exit(1);
		}
		
	}
	
	public static void main(String[] args) {
		
		String inputFile = args[0];
		String outputFile = "SuffixTree.output";
		BufferedReader br = null;
		String in = null;
		long startTime = 0;
		long endTime = 0;
		long timeTaken = 0;

		SuffixTree ukkonen = new SuffixTree();
		
		/*Exit if no input file is specified to the program.*/
		if(args.length == 0){
			System.out.println("usage: java Main <input-file>");
			System.exit(1);
		}
		System.out.println("Using the input file \""+inputFile+"\"");
	
		/* Input string should be stored in the file in single line. 
		   For the test case we assume that the input string can be 
		   read from the file in one go.*/
		
		System.out.println("Reading input from the file");
		
		try{
			br = new BufferedReader(new FileReader(inputFile));
			in = br.readLine();
			if(in == null){
				System.err.println("ERROR: Problem while reading file input.");
				if(br != null) br.close();
				System.exit(1);
			}
			br.close();
		}catch (IOException e) {
			System.err.println("ERROR: Exception occured while reading input file "+e.getMessage());
			System.exit(1);
		}
		in = in + ukkonen.magicCharacter;
		System.out.println("Read the input from the file");
		
		startTime = System.currentTimeMillis();
		
		endTime = System.currentTimeMillis();
		
		timeTaken = endTime - startTime;
		System.out.println("The time taken to construct the suffix tree is "+timeTaken+" milliseconds");
		
		printOutput(outputFile, ukkonen);
		System.out.println("Output Saved to the file \""+outputFile+"\"");
		System.exit(0);
	}
}
