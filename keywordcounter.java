import java.util.*;
import java.io.*;

public class keywordcounter {
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String output = "output_file.txt";

		String key;
		int occurance, remove_max_num, i;
		String[] key_occ;
		HashMap<String, Node> key_occ_map = new HashMap<>();
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		BufferedWriter bw = new BufferedWriter(new FileWriter(output, false));
		String keyword;
		MaxFibanocciHeap max_heap = new MaxFibanocciHeap();
		Node node, max;
		
		while((keyword = br.readLine()) != null) {
			if(keyword.startsWith("$")) {
				key_occ = keyword.substring(1).split(" ");

				key = key_occ[0];
				occurance = Integer.parseInt(key_occ[1]);
				if(!key_occ_map.containsKey(key)) {
					node = new Node(key, occurance);
					key_occ_map.put(key, node);
					max_heap.insert(node);
				}
				else {
					node = key_occ_map.get(key);
					max_heap.increaseKey(node, occurance);
				}
			}
			else if(keyword.startsWith("stop")) {
				br.close();
				bw.close();
				System.exit(0);
			}
			else {
				List<Node> removed_nodes = new ArrayList<Node>();
				remove_max_num = Integer.parseInt(keyword);
				for(i = 0; i < remove_max_num; i++) {
					max = max_heap.removeMax();
					
					removed_nodes.add(max);
					bw.write(max.key);
					if(i != (remove_max_num - 1))
						bw.write(",");
				}
				bw.write("\n");
				for(i = 0; i < remove_max_num; i++)
					max_heap.insert(removed_nodes.get(i));
			}
		}
	}
}
