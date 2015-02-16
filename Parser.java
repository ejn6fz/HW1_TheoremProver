import java.util.Scanner;


public class Parser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Scanner scan = new Scanner (System.in);
		
		String input = null;
		
		while (true) {
		
			input = scan.nextLine();
			
			if (input.contains("Teach")) {
				
				if (input.contains("=")) {
					
					if (input.contains('"')){
						variableInitializer(input);
					}
					else {
						variableBooleanSetter(input);
					}
					
				}
					
					
				if (input.contains("->"))
					commandParser(input);
					
				
			}
			
			
			
			if (input.toLowerCase().equals("stop"))
				break;
			
		}
		
	}
	
	public void variableInitializer (String input) {
		
	}
	
	public void variableBooleanSetter (String input) {
		
	}
	
	public void commandParser (String input ) {
		
	}

}
