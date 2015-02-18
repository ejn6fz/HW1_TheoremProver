import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;


public class Parser {
	
	public static ArrayList<Graph> rules = new ArrayList<Graph>();
	public static ArrayList<String> variables = new ArrayList<String>();
	public static ArrayList<String> knownFacts = new ArrayList<String>();
	public static Hashtable<String, String> definitions = new Hashtable<String, String>();
	public static Hashtable<String, Boolean> boolVals = new Hashtable<String, Boolean>();
	public static Graph current;
	
	public static void main(String[] args) throws FileNotFoundException {

		Scanner scan = new Scanner (System.in);
		String input = null;
		String cmd = null;
		String argument = null;
		
		while (true) {
		
			input = scan.nextLine();
			String cmdArg[] = input.split(" ", 2);
			cmd = cmdArg[0].trim();						// gets the command argument i.e. Teach, List, Learn
			
			if (cmd.equals("Teach")) 
			{
				argument = cmdArg[1].trim();
				
				if (argument.contains("=")) 
				{
					String teach[] =  argument.split("="); 	// split into name and value
					teach[0] = teach[0].trim();				// get rid of leading and trailing whitespace
					teach[1] = teach[1].trim();
					
					if (teach[1].contains("\"")) 
					{
						variableInitializer(teach[0], teach[1]);
					}
					else 
					{
						variableBooleanSetter(teach[0], teach[1]);
					}
				}	
				else if (argument.contains("->")) 
				{
					String teach[] =  argument.split("->"); 	// split into exp and varName
					teach[0] = teach[0].trim();					// get rid of leading and trailing whitespace
					teach[1] = teach[1].trim();
					commandParser(teach[0], teach[1]);
				}

			}
			
			if(cmd.equals("List"))
				list();
			
			if(cmd.equals("Learn"))
				learn();
			
			if (cmd.toLowerCase().equals("stop"))
				break;
		}
	}
	
	public static void variableInitializer (String name, String value) {
		// 	1. Check if the name is already a key in the definitions hashtable
		// 	2. If it is, output that there is already a rule named name
		if(variables.contains(name))
			System.out.println("Variable is already defined: " + name);
		
		// 	3a. If not, add key-value pair (name, value) to definitions hashtable
		// 	3b. Also variableBooleanSetter(name, false)
		else
		{
			variables.add(name);
			definitions.put(name, value);
			boolVals.put(name, Boolean.valueOf("false"));
		}
		//System.out.println("taught: " + name + " = " + value);
		
	}
	
	public static void variableBooleanSetter (String name, String value) {
		// 	1. Check if the name is already a key in the boolVals hashtable
		// 	2. If not, output that the variable must be defined in order to have a value
		if(!boolVals.containsKey(name))
			System.out.println("Variable is not defined: " + name);

		// 	3. If it is, change the existing value of the name key to this value
		else
		{	
			// 4. Add name to the ArrayList of known facts if it isn't already there
			if(value.equals("true"))
			{
				if(!(knownFacts.contains(name)))
					knownFacts.add(name);
				if(knownFacts.contains("!"+name))
					knownFacts.remove("!"+name);
			}
			else
			{
				if(!(knownFacts.contains("!" + name)))
					knownFacts.add("!" + name);
				if(knownFacts.contains(name))
					knownFacts.remove(name);
			}
			boolVals.put(name, Boolean.valueOf(value));
		}
		//System.out.println("taught: " + name + " = " + value);
	}
	
	public static void commandParser (String exp, String varName) {
		String rule = exp + " -> " + varName;

		Vertex root = new Vertex(varName);
		
		current = new Graph(root, rule);
		
		if(rules.contains(current))
			System.out.println("The rule \"" + rule + "\" already exists.");
		else
		{
			reasonBuilder(root, exp);
			
			rules.add(current);
		}
		

	}
	public static boolean reasonBuilder(Vertex v, String reason) {
		
		if (!(reason.contains("!") || reason.contains("&") || reason.contains("|"))) {
			if(reason.charAt(0) == '(' && reason.charAt(reason.length()-1) == ')') {
				reason = reason.substring(1,reason.length()-1);
			}
			Vertex leafNode = new Vertex(reason);
			
			v.addChild(leafNode);
			leafNode.addParent(v);
			current.addLeafNode(leafNode);
			return true;
		}
		
		int parenDepth = 0;
		int orIndex = -1;
		int andIndex = -1;
		int notIndex = -1;
		
		for (int i = 0; i < reason.length(); i++) {
			char c = reason.charAt(i);
			
			if (c == '(')
				parenDepth ++;
			if (c == ')')
				parenDepth --;
			
			if (c == '|' && parenDepth == 0) {
				//return good candidate
				Vertex newNode = new Vertex(c + "");
				v.addChild(newNode);
				newNode.addParent(v);
				
				String reason1 = reason.substring(0,i);
				String reason2 = reason.substring(i+1,reason.length());
				
				return reasonBuilder(newNode, reason1) && reasonBuilder(newNode, reason2); 
			}
			if (c == '&' && parenDepth == 0) {
				//keep track of this in case no |s
				andIndex = i;
			}
			
			if (c == '!' && parenDepth == 0) {
				//keep track of this in case no |s or &s
				notIndex = i;
			}
		}
		
		if (orIndex == -1) {
			if (andIndex != -1) {
				//return good candidate
				char c = reason.charAt(andIndex);
				
				Vertex newNode = new Vertex(c + "");
				v.addChild(newNode);
				newNode.addParent(v);
				
				String reason1 = reason.substring(0,andIndex);
				String reason2 = reason.substring(andIndex+1,reason.length());
				
				return reasonBuilder(newNode, reason1) && reasonBuilder(newNode, reason2);
			}
			else {
				if (notIndex != -1) {
					//return good candidate
					char c = reason.charAt(notIndex);

					
					Vertex newNode = new Vertex(c + "");
					v.addChild(newNode);
					newNode.addParent(v);
					
					//String reason1 = reason.substring(0,notIndex);
					String reason2 = reason.substring(notIndex+1,reason.length());
					
					
					return reasonBuilder(newNode, reason2);
				}
				else {
					//must be contained within parentheses
					reason = reason.substring(1,reason.length()-1);
					return reasonBuilder(v, reason);
				}
			}
			
		}

		return false;
		
	}
	
	public static void list()
	{
		System.out.println("Variables:");
		for(int i = 0; i < variables.size(); i++)
		{
			String var = variables.get(i);
			String val = definitions.get(var);
			System.out.println("\t" + var + " = " + val);
		}
		
		System.out.println("Facts:");
		for(int i = 0; i < knownFacts.size(); i++)
		{
			String var = knownFacts.get(i);
			System.out.println("\t" + var);
		}
		
		System.out.println("Rules:");
		for(int i = 0; i < rules.size(); i++)
		{
			String rule = rules.get(i).reason;
			System.out.println("\t" + rule);
		}
	}
	
	public static void learn(){
		for(int i = 0; i < rules.size(); i++){
			Graph g = rules.get(i);
			Vertex leaf1 = g.leafNodes.get(0);
			
			int b = forwardChaining(leaf1,1);
			if (b == 1) {
				variableBooleanSetter(g.root.value, "true");
			}
		}
	}
	
	public static int forwardChaining(Vertex v, int b) {
		
		if (v.parent == null) {
			return b;
		}
		
		if (!v.value.equals("!") &&  !v.value.equals("&") &&  !v.value.equals("|")) {
			
			if (knownFacts.contains(v.value)) {
				return forwardChaining(v.parent, 1);
				
			} else if (knownFacts.contains("!" + v.value)) {
				
				return forwardChaining(v.parent, 2);
				
			} else {
				return 3;
			}
			
		}
		else if (v.value.equals("!")) {
			
			if (b == 3)
				return forwardChaining (v.parent, b);
			else {
				return forwardChaining (v.parent, (b -2) * -1 + 1);
			}
		}
		else if (v.value.equals("&")) {
			
			int b2 = forwardChainingBackwards(v.children.get(1));
			if (b == 1 && b2 == 1)
				return 1;
			else if (b == 3 || b2 == 3)
				return 3;
			else 
				return 2;
			
		}
		else if (v.value.equals("|")) {
			
			int b2 = forwardChainingBackwards(v.children.get(1));
			if (b == 1 || b2 == 1)
				return 1;
			else if (b == 3 || b2 == 3)
				return 3;
			else 
				return 2;

			
		}
		
		return 3;
	}
	
	public static int forwardChainingBackwards(Vertex v) {
		
		if (!v.value.equals("!") &&  !v.value.equals("&") &&  !v.value.equals("|")) {
			
			if (knownFacts.contains(v.value)) {
				
				return 1;
				
			}
			else if (knownFacts.contains("!" + v.value)) {
				
				return 2;
				
			}
			else {
				return 3;
			}
		}
		else if (v.value.equals("!")) {
			
			int b = forwardChainingBackwards (v.children.get(0));
			if (b == 3)
				return b;
			else 
				return (b -2) * -1 + 1;
			
		}
		else if (v.value.equals("&")) {
			
			int b = forwardChainingBackwards(v.children.get(0));
			int b2 = forwardChainingBackwards(v.children.get(1));
			if (b == 1 && b2 == 1)
				return 1;
			else if (b == 3 || b2 == 3)
				return 3;
			else 
				return 2;
			
		}
		else if (v.value.equals("|")) {
			
			int b = forwardChainingBackwards(v.children.get(0));
			int b2 = forwardChainingBackwards(v.children.get(1));
			if (b == 1 || b2 == 1)
				return 1;
			else if (b == 3 || b2 == 3)
				return 3;
			else 
				return 2;
			
		}
		
		
		return 3;
	}
	

}
