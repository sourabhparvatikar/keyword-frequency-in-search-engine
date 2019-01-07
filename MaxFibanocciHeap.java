import java.util.*;

class MaxFibanocciHeap{
	Node max_pointer = null;  //This class variable always points to the node with max_value;
	int num_nodes = 0;  //Keeps track of number of nodes
	
	public void insert(Node node) {
		
		// if the heap is empty, make the new node as max_pointer
		if(max_pointer == null) {
			max_pointer = node;
			node.left = node;
			node.right = node;
		}
		//else add the new node behind max_pointer
		else {
			node.left = max_pointer.left;
			max_pointer.left.right = node;
			max_pointer.left = node;
			node.right = max_pointer;
			if(node.data > max_pointer.data) {
				max_pointer = node;
			}
		}
		
		num_nodes++;
	}
	
	public Node removeMax() {
		Node max_element = max_pointer;
		if(max_pointer == null) {
			return null;
		}
		
		int num_children = max_pointer.degree;

		Node temp;
		Node curr_child = max_pointer.child;
		
		//add all max_node's children to top level
		while(num_children > 0) {
			temp = curr_child.right;
			
			//remove curr_child from children list
			curr_child.left.right = curr_child.right;
			curr_child.right.left = curr_child.left;
			
			//add curr_child to top level list
			curr_child.right = max_pointer.right;
			max_pointer.right.left = curr_child;
			max_pointer.right = curr_child;
			curr_child.left = max_pointer;
			
			//set parent of curr_child to null
			curr_child.parent = null;
			
			curr_child = temp;
			
			num_children--;
		}
		num_nodes--;
		
		//if no nodes remain after delete_max
		if(num_nodes == 0) {
			max_pointer = null;
			return max_element;
		}
		
		//remove max_pointer
		max_pointer.left.right = max_pointer.right;
		max_pointer.right.left = max_pointer.left;
		
		findNextMax();
		
		if(num_nodes > 1) {
			pairWiseCombine();
		}
		
		max_element.degree = 0;
		max_element.parent = null;
		max_element.child = null;
		max_element.childCutFlag = false;
		max_element.right = max_element;
		max_element.left = max_element;
		
		return max_element;
	}
	
	public void findNextMax() {
		max_pointer = max_pointer.right;
		Node temp = max_pointer;
		Node next_temp;
		int max;
		
		max = max_pointer.data;
		next_temp = max_pointer.right;
		//logic to find the max_element
		while(next_temp != temp) {
			if(next_temp.data >= max){
				max = next_temp.data;
				max_pointer = next_temp;
			}
			next_temp = next_temp.right;
		}
	}
	
	public void pairWiseCombine() {
		
		int curr_degree;
		Node curr_node, exist_node, parent, child, next_node;
		int top_num_nodes = 1;
		HashMap<Integer, Node> degree_table = new HashMap<>();
		
		curr_node = max_pointer.right;
		
		//calculates number of top level nodes
		while(curr_node != max_pointer) {
			top_num_nodes++;
			curr_node = curr_node.right;
		}
		
		curr_node = max_pointer;
		
		//traverse each top level node to perform pair wise combining
		while(top_num_nodes > 0) {
			next_node = curr_node.right;
			curr_degree = curr_node.degree;
			parent = curr_node;
			
			//loop until a tree's degree becomes unique
			while(true) {
				//if no other tree seen till now has curr_degree
				if(!degree_table.containsKey(curr_degree)) {
					degree_table.put(curr_degree, parent);
					break;
				}
				
				//get existing node with curr_degree
				exist_node = degree_table.get(curr_degree);
				
				//assign the greater of 2 nodes to be combined to parent and the smaller to child
				if(exist_node.data >= parent.data) {
					child = parent;
					parent = exist_node;
				}
				else
					child = exist_node;
				
				//make child node the child of parent
				makeChild(parent, child);
				
				//update degree table
				degree_table.remove(curr_degree);
				
				//increment degree
				curr_degree++;
				
			}
			curr_node = next_node;
			top_num_nodes--;
		}
	}
	
	public void makeChild(Node parent, Node child) {
		
		//remove child from root level
		child.left.right = child.right;
		child.right.left = child.left;
		child.parent = parent;
		
		//if parent did not have any child
		if(parent.child == null) {
			parent.child = child;
			child.right = child;
			child.left = child;
		}
		//if parent had children
		else {
			child.right = parent.child.right;
			child.left = parent.child;
			parent.child.right = child;
			child.right.left = child;
		}
		
		//increase degree of parent
		parent.degree++;
		
		//mark child's child cut field as false
		child.childCutFlag = false;
	}
	
	public void increaseKey(Node node, int data) {
		
		//increase data of node by specified amount
		node.data = node.data + data;
		Node parent = node.parent;
		
		//perform cut if node is not in top level and data becomes greater its parent's data
		if(parent != null && node.data > parent.data) {
			cut(parent, node);
		}
		
		//check if the increased key is greater than max_pointer
		if(node.data > max_pointer.data) {
			max_pointer = node;
		}
	}
	
	public void cut(Node parent, Node child) {
		
		parent.degree--;
		
		//update child pointer of parent if necessary
		if(parent.degree == 0) {
			parent.child = null;
		}
		else {
			if(parent.child == child)
				parent.child = child.right;
			
			//remove child from its sibling list
			child.left.right = child.right;
			child.right.left = child.left;
		}
		
		
		//move child to root list
		child.right = max_pointer.right;
		child.left = max_pointer;
		child.right.left = child;
		max_pointer.right = child;
		child.parent = null;
		
		//set child's childCutFlag to false
		child.childCutFlag = false;
		
		//perform cascading cut if necessary
		child = parent;
		parent = parent.parent;
		if(parent != null) {
			if(!child.childCutFlag)
				child.childCutFlag = true;
			else{
				cut(parent, child);
			}
		}
	}
}
