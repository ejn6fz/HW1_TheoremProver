import java.util.*;


public class Graph {

	Vertex root;
	LinkedList<Vertex> leafNodes;
	String reason;
	
	public Graph(Vertex root, String reason) {
		this.root = root;
		leafNodes = new LinkedList<Vertex>();
		this.reason = reason; 
	}	
	
	
	public boolean addLeafNode (Vertex v) {
		
		if (leafNodes.contains(v))
			return false;
		
		return leafNodes.add(v);
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Graph)){
			return false;
		}
		Graph other = (Graph) o;
		
		return this.reason.equals(other.reason);
		
	}
	
	
}
