package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		TrieNode root = new TrieNode(null, null, null);
		Indexes firstWord = new Indexes ( 0, (short) 0, (short) (allWords[0].length()-1) );
		root.firstChild = new TrieNode( firstWord, null, null );
		int lastMatchIndex;
		for ( int indexWord = 0; indexWord < allWords.length; indexWord++ ) {
			lastMatchIndex = -1;
			TrieNode prev = root;
			TrieNode current = root.firstChild;
			boolean addSibling = false;
			boolean newPrefixNode = false;
			do { //search where node should be inserted
				boolean fullMatch = false;
				int sI = (int)current.substr.startIndex;
				int eI = (int) current.substr.endIndex;
				int i = sI;
				if( allWords[current.substr.wordIndex].charAt(sI) == allWords[indexWord].charAt(sI) ) {
					for( ; i<=eI; i++ ) { //finds ending index for the match
						if( allWords[current.substr.wordIndex].charAt(i) == allWords[indexWord].charAt(i) ) {
							if( i == eI ) {
								fullMatch = true;
							}
							} else break;
						}
					if( !fullMatch ) {//if its not a full match this means a new prefix node needs to be made here
						lastMatchIndex = i-1;
						newPrefixNode = true;
						break;
					} else { //if it is a full match this means the node it mathced with is a prefix and it will move down to its childern
						prev = current;
						current = current.firstChild;
					}
				} else {
					prev = current;
					current = current.sibling;
					if( current == null ) {
						addSibling = true;
					}
				}
			} while( current != null );
			//now will create new node(s)
			if( newPrefixNode ) {
				int index = current.substr.wordIndex;
				short sI = current.substr.startIndex;
				Indexes Prefix = new Indexes( index, sI, (short) lastMatchIndex );
				Indexes cur = new Indexes( current.substr.wordIndex, (short) (lastMatchIndex+1), current.substr.endIndex );
				current.substr = cur;
				TrieNode PrefixNode = new TrieNode( Prefix, current, current.sibling);
				if( prev.firstChild == current ) {
					prev.firstChild = PrefixNode;
				} else {
					prev.sibling = PrefixNode;
					
				}
				Indexes nWIdx = new Indexes( indexWord, (short)(lastMatchIndex+1), (short) (allWords[indexWord].length()-1) );
				TrieNode newWord = new TrieNode( nWIdx, null, null );
				current.sibling = newWord;
			}
			if( addSibling ) {
				if( lastMatchIndex == -1 ) {
					lastMatchIndex = allWords[indexWord].length()-1;
				}
				short sI = prev.substr.startIndex;
				Indexes newWI = new Indexes( indexWord, sI, (short)lastMatchIndex);
				prev.sibling = new TrieNode (newWI, null, null);
			}
			
		}
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		return root;
	}
	
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		ArrayList <TrieNode> matches = new ArrayList<TrieNode>();
		TrieNode current = root.firstChild;
		TrieNode match = null;
		do {
			boolean fullMatch = false;
			int sI = (int)current.substr.startIndex;
			int eI = (int) current.substr.endIndex;
			boolean checkFull = false;
			boolean prefixSmall = false;
			if ( eI >= prefix.length()-1) {
				checkFull = true;
				if ( eI> prefix.length()-1 ) {
					prefixSmall=true;
				}
			}
			int i = sI;
			if( allWords[current.substr.wordIndex].charAt(sI) == prefix.charAt(sI) ) {
				if ( prefixSmall ) { //prefix is smaller than the substring
					for( ; i<prefix.length(); i++ ) {
						if( allWords[current.substr.wordIndex].charAt(i) == prefix.charAt(i) ) {
							if (i == prefix.length()-1) {
								fullMatch = true;
							}
							continue;
						} else {
							break;
						}
					}
					if (fullMatch) {
						match = current;
						break;
					} else {
						return null;
					}
				}else if (checkFull) { //prefix is the same size as substring
					for( ; i<=eI; i++ ) { //finds ending index for the match
						if( allWords[current.substr.wordIndex].charAt(i) == prefix.charAt(i) ) {
							if( i == eI) {
								fullMatch = true;
							}
							continue;
						}else break;
					}
					if (fullMatch) {
						match = current;
						break;
					} else { 
						return null;
					}
				} else { //prefix is larger than substring
					for( ; i<=eI; i++ ) { //finds ending index for the match
						if( allWords[current.substr.wordIndex].charAt(i) == prefix.charAt(i) ) {
							if( i == eI) {
								current = current.firstChild;
							}
						} else return null; //here if it matches with the starting index but does not match with one of the other chars in the substring it means that the given prefix is not in the tree & will return empty AL
					}
				}
			} else { 
				current= current.sibling;
			}
		
		}while( current != null);
		if (match == null) {
			return null;
		}
		if ( match.firstChild==null) { //prefix is a word in the tree or only matched one word
			matches.add(match);
			return matches;
		}else {
			TrieNode curPtr = match.firstChild;
			matches = traverse(curPtr);
			if(matches.isEmpty()) {
				return null;
			}
		}
		return matches;
	}
	private static ArrayList<TrieNode> traverse (TrieNode root){
		ArrayList <TrieNode> matches = new ArrayList<TrieNode>();
		
		if(root.firstChild != null) {
			matches.addAll(traverse(root.firstChild));
		}else {
			matches.add(root);
		}
		if (root.sibling != null) {
			matches.addAll(traverse(root.sibling));
		}
		return matches;
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
