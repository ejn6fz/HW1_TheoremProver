import java.util.*;


public class Graph {

	Vertex root;
	LinkedList<Vertex> endNodes;
	
	public Graph(Vertex root) {
		this.root = root;
		endNodes = new LinkedList<Vertex>();
	}
	
	public boolean addEndNode (Vertex v) {
		
		if (endNodes.contains(v))
			return false;
		
		return endNodes.add(v);
	}
	
	
	
}
