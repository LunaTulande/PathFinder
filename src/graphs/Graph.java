package graphs;
import java.util.*;

public interface Graph<X> 
{
    void add(X ny);
    void connect(X from, X to, String name, int time);
    void setConnectionWeight(X from, X to, int newTime);
    List<X> getNodes();
    List<Edge<X>> getEdgesFrom (X from);
    Edge<X> getEdgeBetween(X from, X to);
}