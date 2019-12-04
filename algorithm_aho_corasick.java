package patternMatching_1;
import java.util.*;

class node{
	Map<Character, node> hs; // contain link to child nodes
	char node_character; // present nodes character
	boolean is_end; // this helps to determine if we have found a pattern or not
	boolean im_root; // this is true for the root alone, rest all nodes are false
	node parent_node; // link to parent of the node
	node fail_link; // link to node which have similar substring pattern
	node is_pattern; // if node is the end, will point to itself else will point to root
	String word_formed; // word formed
	public node(){
		hs = new HashMap();
		is_end = false;
		im_root = false;
	}
}

public class algorithm_aho_corasick {
	
	// creating root - root will not have any character
	node root;
	public algorithm_aho_corasick() {
		root = new node();
		root.im_root = true;
	}
	
	// building Trie for the given pattern
	public void build_trie(String word) {
		
		node cur = root;
		String temp_word = "";
		for(int i =0; i < word.length(); i++) {
			char letter = word.charAt(i);
			temp_word = temp_word + letter;
			node n1 = cur.hs.get(letter);
			if(n1 == null) {
				n1 = new node();
				n1.parent_node = cur; // point to previous node in which it is child
				n1.node_character = letter; 
				n1.word_formed = temp_word; // the word formed till this point
				cur.hs.put(letter, n1); // add child to parent
			}
			cur = n1;
		}
		cur.is_end = true; // end of pattern
	}
	
	// creating fail link
	public void fail_similar_link_creation(){
		
		node cur = root;
		cur.fail_link = root; // root and its child will have fail link pointed to root
		cur.is_pattern = root;
		Queue<node> node_q = new LinkedList<>();
		for(Character c: cur.hs.keySet()) {
			node n1 = cur.hs.get(c);
			n1.fail_link = cur;
			if(n1.is_end) {
				n1.is_pattern = n1;
			}else {
				n1.is_pattern = cur;
			}
			for(Character r: n1.hs.keySet()) {
				node_q.add(n1.hs.get(r)); // add the present node child to queue
			}
		}
		
		while(node_q.size() > 0) {
			node n1 = node_q.poll();
			node parent_fail = n1.parent_node.fail_link;
			char present_node_char = n1.node_character;
			while(true) {
				if(parent_fail.hs.containsKey(present_node_char)) {
					// node should point to its parent fail node's child with same character
					n1.fail_link = parent_fail.hs.get(present_node_char);
					break;
				}
				// if parent fail node does not have child with same character point to root
				if(parent_fail.im_root) {
					n1.fail_link = root;
					break;
				}
				// if parent fail node does not have child with same character
				// search till you reach root
				parent_fail = parent_fail.fail_link;
			}
			
			if(n1.is_end) {
				n1.is_pattern = n1; // if end point to itself
			}else {
				n1.is_pattern = n1.fail_link.is_pattern; // if not end point to root
			}
			for(Character c: n1.hs.keySet()) {
				node_q.add(n1.hs.get(c)); // add the present node child to queue
			}
		}
	}
	
	// Finding the patterns in the text
	public void find_pos(String text) {
		node cur = root;
		int result = 0;
		for(int i = 0; i < text.length(); i++) {
			while(true) {
				if(cur.hs.containsKey(text.charAt(i))) {
					// if character was found in node's child
					// then check if that node is the end
					cur = cur.hs.get(text.charAt(i));
					break;
				}
				if(cur.im_root) break;
				// if character was not found in node's child
				// then try searching the fail link child till you get to root 
				cur = cur.fail_link;
			}
			node temp = cur;
			while(true) {
				// is_pattern will point to root if it is not end of a pattern
				temp = temp.is_pattern;
				if(temp.im_root) break;
				// Output - index of the pattern found
				System.out.println("Found word and index:");
				System.out.print(temp.word_formed);
				System.out.print(" -> ");
				System.out.println((i)-((temp.word_formed.length())-1));
				System.out.println();
				// search in fail link if you have any substring of the pattern just found
				// like zyyz text will have zyyz and yyz as pattern
				temp = temp.fail_link;
			}
		}
		//System.out.println(result);
	}
	
	// Main
	public static void main(String[] args) {
		String[] substrings = new String[4];
		substrings[0] = "zyyz";
		substrings[1] = "xzy";
		substrings[2] = "yzyz";
		substrings[3] = "yyz";
		algorithm_aho_corasick obj = new algorithm_aho_corasick();
		// building the trie data structure
		for(int i = 0; i < substrings.length; i++) {
			obj.build_trie(substrings[i]);
		}
		// create the fail link and similar word link
		obj.fail_similar_link_creation();
		String input_string = "zzyyzxzyzxzyzyz";
		System.out.println("Input Text:");
		System.out.println(input_string);
		obj.find_pos(input_string);
	}
}

