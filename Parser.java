import java.util.Scanner;


public class Parser {

	public static void main(String[] args) {

		Scanner scan = new Scanner (System.in);
		
		String input = null;
		String cmd = null;
		String argument = null;
		
		while (true) {
		
			input = scan.nextLine();
			String cmdArg[] = input.split(" ", 2);
			cmd = cmdArg[0].trim();					// gets the command argument i.e. Teach, List, Learn
			argument = cmdArg[1].trim();
			
			if (cmd.equals("Teach")) {
				
				if (argument.contains("=")) {
					
					String teach[] =  argument.split("="); 	// split into name and value
					teach[0] = teach[0].trim();		// get rid of leading and trailing whitespace
					teach[1] = teach[1].trim();
					
					if (teach[1].contains("\"")){
						variableInitializer(teach[0], teach[1]);
					}
					else {
						variableBooleanSetter(teach[0], teach[1]);
					}
					
				}
					
				else if (argument.contains("->")) {
					String teach[] =  argument.split("->"); // split into exp and varName
					teach[0] = teach[0].trim();		// get rid of leading and trailing whitespace
					teach[1] = teach[1].trim();
					commandParser(teach[0], teach[1]);
				}
					
			}
			
			if (cmd.toLowerCase().equals("stop"))
				break;
			
		}
		
	}
	
	public void variableInitializer (String name, String value) {
		
	}
	
	public void variableBooleanSetter (String name, String value) {
		
	}
	
	public void commandParser (String exp, String varName) {
		
	}

}
