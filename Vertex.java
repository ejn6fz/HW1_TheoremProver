import java.util.LinkedList;


public class Vertex {

	String value;
	public Vertex parent;
	public LinkedList <Vertex> children;
	
	public Vertex(String value) {
		this.value = value;
		children = new LinkedList<Vertex>();
		parent = null;
	}
	
	public void addParent(Vertex v) {
		parent = v;		
	}
	
	public boolean addChild(Vertex v){
		return children.add(v);
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Vertex)){
			return false;
		}
		Vertex other = (Vertex) o;
		
		return this.value.equals(other.value);
		
	}
	
}
