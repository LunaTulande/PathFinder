package graphs;
public class Edge<N>
{
    private N destination;
    private String transportName;
    private int time;
    
    public Edge(N destination, String name, int time){
        this.destination= destination;
        this.transportName= name;
        this.time= time; }
    
    public N getDestination() { return destination; }
    
    public String getName() { return transportName; }
    
    public int getWeight() { return time; }
    
    public void setTime(int newTime){
        if(newTime <0){
            throw new IllegalArgumentException("Time must be greater that zero"); }
        else{
            time= newTime; } 
    }
    
    public String toString(){ 
        return transportName+ " to " +destination+ " (" +time+ ")"; }
}