package graphs;
import java.util.*;

public class ListGraph<N> implements Graph<N>
{
    private Map<N, List<Edge<N>>> paths= new HashMap<N, List<Edge<N>>>();
    
    @Override
    public void add(N newPlats){
        if(!paths.containsKey(newPlats)){
            paths.put(newPlats, new ArrayList<Edge<N>>());}
    }
    
    @Override
    public void connect(N from, N to, String transportName, int time){
        if(time <0){
            throw new IllegalArgumentException("Time must be greater that zero");}
        if(!paths.containsKey(from) || !paths.containsKey(to)){
            throw new NoSuchElementException("City does not exist");}
        
        List<Edge<N>> fromWays= paths.get(from);
        List<Edge<N>> toWays= paths.get(to);
        
        Edge<N> eFrom= new Edge<N>(to, transportName, time);
        for(Edge<N> e : fromWays){
            if(e.equals(eFrom)){
                throw new IllegalStateException("Connection already exists"); }
        }
        fromWays.add(eFrom);
        
        Edge<N> eTo= new Edge<N>(from, transportName, time);
        for(Edge<N> e : toWays){
            if(e.equals(eTo)){
                throw new IllegalStateException("Connection already exists");}
        }
        toWays.add(eTo);
    }//connect
    
    @Override
    public void setConnectionWeight(N from, N to, int newTime){
        if(newTime <0){
            throw new IllegalArgumentException("Time must be greater that zero");}
        if(!paths.containsKey(from) || !paths.containsKey(to)){
            throw new NoSuchElementException("City does not exist");}
        
        List<Edge<N>> fromWays= paths.get(from);
        List<Edge<N>> toWays= paths.get(to);
        
        boolean find1= false;
        for(Edge<N> e : fromWays){
            if(e.getDestination().equals(to)){
                e.setTime(newTime);
                find1= true;
                break;
            }
        }
        
        if(!find1)
        { throw new NoSuchElementException("No connection between these two nodes"); }
        
        boolean find2= false;
        for(Edge<N> e : toWays){
            if(e.getDestination().equals(from)){
                e.setTime(newTime);
                find2= true;
                break;
            }
        }
        
        if(!find2)
        { throw new NoSuchElementException("No connection between these two nodes"); }
        
    }//setConnectionWeight
    
    @Override
    public List<N> getNodes(){
        return new ArrayList<N>(paths.keySet()); }
    
    @Override
    public List<Edge<N>> getEdgesFrom (N from){
        if(!paths.containsKey(from)){
            throw new NoSuchElementException("City does not exist"); }
        return new ArrayList<Edge<N>>(paths.get(from)); 
    }
    
    @Override
    public Edge<N> getEdgeBetween(N from, N to){
        if(!paths.containsKey(from) || !paths.containsKey(to)){
            throw new NoSuchElementException("City does not exist"); }
        
        for(Edge<N> e : paths.get(from)){
            if(e.getDestination().equals(to))
            {   return e; }
        }
        return null;
    }
      
    @Override
    public String toString(){
        String str="";            
        for(Map.Entry<N, List<Edge<N>>> me : paths.entrySet()){
            N plats= me.getKey();
            List<Edge<N>> connections= me.getValue();
            str+= plats.toString()+ ": " +connections+ "\n";
        }
        return str;
    }
}//ListGraph