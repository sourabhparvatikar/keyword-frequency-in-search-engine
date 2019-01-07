public class Node {
	public int degree = 0;
	public Node parent = null;
	public Node child = null;
	public int data;
	public boolean childCutFlag;
	public Node left = null;
	public Node right = null;
	String key;
	
	Node(String key, int data) {
		this.key = key;
		this.data = data;
	}
}
