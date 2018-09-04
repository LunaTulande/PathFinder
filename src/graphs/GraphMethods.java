package graphs;
import java.util.*;

public class GraphMethods 
{
    static private <N> void depthFirstSearchExist(Graph<N> g, N from, Set<N> visited){
        visited.add(from);
        for(Edge<N> e : g.getEdgesFrom(from)){
            if(!visited.contains(e.getDestination())){
                depthFirstSearchExist(g, e.getDestination(), visited); }
        }
    }
    
    static public <N> boolean pathExist(Graph<N> g, N from, N to){
        Set<N> visited= new HashSet<N>();
        depthFirstSearchExist(g, from, visited);
        return visited.contains(to); 
    }
    
    static public <N> List<Edge<N>> getPath(Graph<N> g, N from, N to){
        if(pathExist(g, from, to)){
            Map<N, Integer> calculateTime= new HashMap<N, Integer>();
            Map<N, Boolean> determined= new HashMap<N, Boolean>();
            Map<N, N> via= new HashMap<N, N>();

            Iterator<N> iter= g.getNodes().iterator();
            while(iter.hasNext())
            {
                N pl= iter.next();
                if(pl.equals(from))
                { calculateTime.put(pl, 0);
                    determined.put(pl, true); }
                else
                { calculateTime.put(pl, Integer.MAX_VALUE);
                  determined.put(pl, false); }
                via.put(pl, null);
            }
            N current= from;
            while(!determined.get(to))
            {
                for(Edge<N> e : g.getEdgesFrom(current)){
                    if(!determined.get(e.getDestination())){
                        int time= e.getWeight() + calculateTime.get(current);
                        if(time < calculateTime.get(e.getDestination()))
                        { calculateTime.put(e.getDestination(), time);
                          via.put(e.getDestination(), current); }
                    }//if
                }//for
                int minTime= Integer.MAX_VALUE;
                N reference= null;
                for(N p : determined.keySet()){
                    if(!determined.get(p)){
                        int time= calculateTime.get(p);
                        if(time < minTime)
                        { minTime= time;
                          reference= p; }
                    }//if
                }//for
                determined.put(reference, true);
                current= reference;
            }//while
            
            LinkedList<Edge<N>> fastPath= new LinkedList<Edge<N>>();
            N end= to;
            while(!end.equals(from)){
                N whereFrom= via.get(end);
                fastPath.addFirst(g.getEdgeBetween(whereFrom, end));
                end= whereFrom;
            }
            return fastPath;
        }//if pathExist from from to to
        else
            return null;
    }//getPath
}//GraphMethods