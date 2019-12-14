
package friends;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;



public class Driver {

	public static void main(String[] args) 
			throws IOException {
		
		// TODO Auto-generated method stub

		System.out.print("Enter words file name => ");
		Scanner in = new Scanner(System.in);
		String s = in. nextLine();
		Scanner sc = new Scanner(new File(s));
		System.out.println("file made");
		Graph x = new Graph (sc);
//		for (int z = 0; z<x.members.length; z++) {
//			System.out.println(x.members[z].name+"| "+x.members[z].student);
////			if(x.members[z].student) {
////				System.out.print("| "+x.members[z].school);
////			}
//			Friend ptr = x.members[z].first;
//			while(ptr!=null) {
//				String l = x.members[ptr.fnum].name;
//				System.out.print("-->"+ l);
//				ptr=ptr.next;
//			}
//			System.out.println("\n" + "****");
//		}
		
		System.out.println(Friends.shortestChain(x, "sam", "samir"));
		System.out.println(Friends.cliques(x,"rutgers"));
		ArrayList<String> cnnt = Friends.connectors(x);
		System.out.println(cnnt);
		
		
		
	
		
	}
	

}
