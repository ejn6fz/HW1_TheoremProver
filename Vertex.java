import java.util.LinkedList;


public class Vertex {

	String value;
	public LinkedList <Vertex> upstream;
	public LinkedList <Vertex> downstream;
	
	public Vertex(String value) {
		this.value = value;
	}
	
	public boolean addDownstream(Vertex v) {
		return downstream.add(v);
	}
	
	public boolean addUpstream(Vertex v){
		return upstream.add(v);
	}
	
}
