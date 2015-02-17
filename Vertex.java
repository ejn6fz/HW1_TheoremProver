import java.util.LinkedList;


public class Vertex {

	String value;
	public Vertex parent;
	public LinkedList <Vertex> children;
	
	public Vertex(String value) {
		this.value = value;
		children = new LinkedList<Vertex>();
	}
	
	public void addParent(Vertex v) {
		parent = v;		
	}
	
	public boolean addChild(Vertex v){
		return children.add(v);
	}
	
}
