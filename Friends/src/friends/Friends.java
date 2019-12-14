package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		/** COMPLETE THIS METHOD **/
		ArrayList<String> shrtst= new ArrayList<>();
		boolean [] visited = new boolean[g.members.length];
		int [] previous = new int [g.members.length];
		int start =-1;
		int target = -1;
		for (int i =0 ; i<g.members.length; i++) {
			visited[i]= false;
			previous[i]=-1;
			if (g.members[i].name.contentEquals(p1)) {
				start = i;
			}
			if (g.members[i].name.contentEquals(p2)) {
				target = i;
			}
		}
		if(start==-1||target==-1) {
			return shrtst;
		}
		Queue<Integer> bfsQ = new Queue<>();
		bfsQ.enqueue(start);
		visited[start]=true;
		while(!bfsQ.isEmpty()) {
			int dQ =bfsQ.dequeue();
			Friend ptr = g.members[dQ].first;
			for (; ptr!=null; ptr=ptr.next) {
				if(!visited[ptr.fnum]) {
					bfsQ.enqueue(ptr.fnum);
					visited[ptr.fnum]=true;
					previous[ptr.fnum]=dQ;
				}
				if(ptr.fnum==target) {
					break;
				}
			}
			if(visited[target]) {
				break;
			}
		}
		if(!visited[target]) {
			return shrtst;
		}
		Stack<String> bkW = new Stack<>();
		int prev = target;
		while(prev!= -1) {
			bkW.push(g.members[prev].name);
			prev = previous[prev];
		}
		while(!bkW.isEmpty()) {
			shrtst.add(bkW.pop());
		}
		if(shrtst.size()==1) {
			shrtst.remove(0);
		}
		
		return shrtst ;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		/** COMPLETE THIS METHOD **/
		ArrayList<ArrayList<String>> cliQs = new ArrayList<>();
		boolean [] visited = new boolean[g.members.length];
		boolean [] sameSch = new boolean[g.members.length];
		for (int i =0 ; i<g.members.length; i++) {
			visited[i]= false;
			if(g.members[i].student) {
				if(g.members[i].school.equals(school)) {
					sameSch[i]=true;
				}else sameSch[i]=false;
			}else sameSch[i]=false;
		}
		
		for( int i =0; i<g.members.length; i++ ) {
			if(visited[i]||!sameSch[i]) {
				visited[i] = true;
				continue;
			}
			Queue<Person> bfsQ = new Queue<>();
			ArrayList<String> cliq = new ArrayList<>();
			bfsQ.enqueue(g.members[i]);
			cliq.add(g.members[i].name);
			while(!bfsQ.isEmpty()) {
				Friend x = bfsQ.dequeue().first;
				for(; x!=null; x=x.next) {
					if(visited[x.fnum]||!sameSch[x.fnum]) {
						visited[x.fnum]=true;
						continue;
					}else {
						visited[x.fnum]=true;
						if (!cliq.contains(g.members[x.fnum].name)) { cliq.add(g.members[x.fnum].name);}
						bfsQ.enqueue(g.members[x.fnum]);
					}	
					}
				}
			cliQs.add(cliq);
			
			}
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return cliQs;
		
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		/** COMPLETE THIS METHOD **/
		
		
		ArrayList<String> cnnt = new ArrayList<>();
		boolean [][] graph = new boolean [g.members.length][g.members.length];
		boolean [] visited =  new boolean [g.members.length];
		int [] dfsnum = new int [g.members.length];
		int [] back = new int [g.members.length];
		int [] parent = new int [g.members.length];
		boolean [] isConnect = new boolean [g.members.length];
		int vertex = 0;
		for( int i = 0; i<g.members.length; i++) {
			for( Friend ptr = g.members[i].first; ptr!=null; ptr=ptr.next ) {
				graph[i][ptr.fnum]=true;
			}
			visited[i]=false;
			isConnect[i]=false;
			dfsnum[i]=0;
			back[i]=Integer.MAX_VALUE;
			parent[i]=-1;
		}
		for(int i = 0;i<g.members.length; i++) {
			if(visited[i]) {
				continue;
			}
			int time = 1;
			connectors(g,graph,visited,dfsnum,back,parent,isConnect,i,time);	
		}
		for( int i =0; i<isConnect.length;i++) {
			if(isConnect[i]) {
				cnnt.add(g.members[i].name);
			}
		}
		
		
		return cnnt;
		
		
		
	}
	private static void connectors (Graph g, boolean [][] graph, boolean[] visited, int[] dfsnum, int [] back, int [] parent, boolean [] isConnect, int vertex, int time) {
		visited[vertex]=true;
		
		dfsnum[vertex] = time;
		back[vertex]=time;
		int children =0;
		for ( int i =0;i<graph.length; i++) {
			if(graph[vertex][i]==true) {
				if (!visited[i]) {
					children = children+1;
					parent[i]=vertex;
					connectors(g,graph,visited,dfsnum,back,parent,isConnect,i,time+1);
					time=time+1;
					back[vertex] = Math.min(back[vertex], back[i]);
					if(parent[vertex]==-1 && children>1) {
						isConnect[vertex]=true;
					}
					if(parent[vertex]!= -1 && back[i]>=dfsnum[vertex]) {
						isConnect[vertex]=true;
					}
				}else if (parent[vertex]!= i) {
					back[vertex]=Math.min(back[vertex], dfsnum[i]);
				}
			}
		}
		return;
	}


}

