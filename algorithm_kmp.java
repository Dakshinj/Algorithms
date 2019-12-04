package patternMatcing;

public class algorithm_kmp {
	
	private void kmp_search(String text, String pattern) {
		
		// Pre-processing
		int[] auxiliary_arr = new int[pattern.length()];
		int j = 0;
		auxiliary_arr[0] = 0;
		for(int i  = 1; i < pattern.length(); ) {
			if(pattern.charAt(i) == pattern.charAt(j)) {
				// if the suffix and prefix match
				// the index + 1 will be stored. to make sure a similar pattern is not compared again
				auxiliary_arr[i++] = 1 + j++;
			}else {
				if(j == 0) {
					// if the suffix and prefix does not match
					// the pattern index will start from 0
					auxiliary_arr[i++] = 0;
				}else {
					// search for new prefix
					j = auxiliary_arr[j-1];
				}
			}
		}
		// print the auxiliary array
		for(int i = 0; i<auxiliary_arr.length; i++) {
			System.out.print(auxiliary_arr[i] + " ");
		}
		System.out.println("\n");
		
		// String matching
		j=0;
		for(int i = 0; i<text.length() && j<pattern.length();) {
			if(text.charAt(i) == pattern.charAt(j)) {
				// if the characters match between text and pattern both the index is incremented
				i++;
				j++;
			}else {
				if(j == 0) {
					// if they do not match i alone is incremented
					// when there is no prefix and suffix match in the substring of the pattern
					i++;
				}else {
					// search for new prefix 
					j = auxiliary_arr[j-1];
				}
			}
			// Pattern found
			if(j == pattern.length()) {
				System.out.print("found at index: ");
				System.out.println(i-pattern.length());
				return;
			}
		}
		System.out.println("Not found");
	}
	// Main
	public static void main(String[] args) {
		String text = "xzwxyzwxyzwxywzxyw";
		String pattern = "wxyzwxyw";
		//String text = "gcgtacgcagagagtataagtacgcagacg";
		//String pattern = "agtacg";
		algorithm_kmp obj = new algorithm_kmp();
		//System.out.println();
		System.out.println("Input string:");
		System.out.println(text);
		System.out.println();
		System.out.println("pattern:");
		System.out.println(pattern);
		System.out.println();
		System.out.println("Auxiliary array:");
		obj.kmp_search(text, pattern);
	}
}
