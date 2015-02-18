Ryan Mahoney - rtm8mg
Elliot Nolan - ejn6fz

To execute the code:
	1. Run "make"
	2. Run "java Parser"
	3. Enter input for the program

Sample Input:

	Teach A = "aaa"
	Teach B = "bbb"
	Teach C = "ccc"
	Teach D = "ddd"
	Teach A = true
	Teach B = false
	Teach A&!B -> C
	Teach A&C -> D
	Teach E = "eee"
	Teach (A|B)&C|!D&(A|(B&C)) -> E
	Why E
	Learn
	List
	
Sample Output:

	true
	I KNOW THAT aaa
	I KNOW IT IS NOT TRUE THAT bbb
	I KNOW THAT aaa
	I KNOW IT IS NOT TRUE THAT bbb
	BECAUSE IT IS TRUE THAT aaa AND IT IS NOT TRUE THAT bbb I KNOW THAT ccc
	I KNOW THAT aaa
	I KNOW THAT aaa
	I KNOW IT IS NOT TRUE THAT bbb
	BECAUSE IT IS TRUE THAT aaa AND IT IS NOT TRUE THAT bbb I KNOW THAT ccc
	BECAUSE IT IS TRUE THAT aaa AND ccc I KNOW THAT ddd
	I KNOW THAT aaa
	I KNOW IT IS NOT TRUE THAT bbb
	I KNOW THAT aaa
	I KNOW IT IS NOT TRUE THAT bbb
	BECAUSE IT IS TRUE THAT aaa AND IT IS NOT TRUE THAT bbb I KNOW THAT ccc
	BECAUSE IT IS TRUE THAT (aaa OR bbb) AND ccc OR IT IS NOT TRUE THAT ddd AND (aaa OR (bbb AND ccc)) I KNOW THAT eee
	THUS I KNOW THAT eee
	Variables:
		A = "aaa"
		B = "bbb"
		C = "ccc"
		D = "ddd"
		E = "eee"
	Facts:
		A
		!B
		C
		D
		E
	Rules:
		A&!B -> C
		A&C -> D
		(A|B)&C|!D&(A|(B&C)) -> E
		
How the Why command works:
	When a user prompts for a Why command, the system parses the expression argument into a graph (essentially a tree...) and executes a backward chaining process. It does
	this by calling the backwardChainingWhy method. This has a base case in which the token (technically a substring) is checked to be a true value in the knownFacts list.
	If so, it returns true and appends it's output string to the string that will be printed to the output at the very end of the process. If not, the list of known rules 
	is searched to determine whether there are any rules that cause the value of the token to be set to true. It goes through the expressions of such rules and recursively 
	calls backwardChainingWhy on the child of the root element in the graph. Our code differs from what might be seen as the "normal" implementation in that we allow for a
	variable to be set to false through Teach. This way, a variable, v, can be in the known facts as it's opposite, !v. This allows for a value of false to hold a meaning 
	of either a) v is not known to be true or b) v is known to be false. Because of this route taken during implementation, we had to restructure the recursive 
	backwardChainingWhy function to return an int instead of a boolean, recursively. If a call returned a 1, the fact was known as true. Returning a 2 represented the fact
	being known as false, while a 3 represented a fact not being known as true. It is in this way that the NOT operator can serve its full functionality. 
