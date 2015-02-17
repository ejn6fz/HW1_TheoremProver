import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;


public class Parser {
	
	public static ArrayList<String> rules = new ArrayList<String>();
	public static ArrayList<String> variables = new ArrayList<String>();
	public static ArrayList<String> knownFacts = new ArrayList<String>();
	public static Hashtable<String, String> definitions = new Hashtable<String, String>();
	public static Hashtable<String, Boolean> boolVals = new Hashtable<String, Boolean>();
	
	public static void main(String[] args) {

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
		if(rules.contains(rule))
			System.out.println("The rule \"" + rule + "\" already exists.");
		else
		{
			rules.add(rule);
		}
	}
	
		public static boolean reasonBuilder(Vertex v, String reason) {
		
		if (!(reason.contains("!") | reason.contains("&") | reason.contains("|"))) {
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
				String reason2 = reason.substring(i,reason.length());
				
				return reasonBuilder(newNode, reason1) && reasonBuilder(newNode, reason2); 
			}
			if (c == '&' && parenDepth == 0) {
				//keep track of this in case no |s
				andIndex = i;
			}
			
			if (c == '!' && parenDepth == 0) {
				//keep track of this in case no |s or &s
				orIndex = i;
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
				String reason2 = reason.substring(andIndex,reason.length());
				
				return reasonBuilder(newNode, reason1) && reasonBuilder(newNode, reason2);
			}
			else {
				if (notIndex != -1) {
					//return good candidate
					char c = reason.charAt(notIndex);

					
					Vertex newNode = new Vertex(c + "");
					v.addChild(newNode);
					newNode.addParent(v);
					
					String reason1 = reason.substring(0,notIndex);
					String reason2 = reason.substring(notIndex,reason.length());
					
					return reasonBuilder(newNode, reason1) && reasonBuilder(newNode, reason2);
				}
				else {
					//must be contained within parentheses
					reason = reason.substring(1,reason.length());
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
			String rule = rules.get(i);
			System.out.println("\t" + rule);
		}
	}

}
