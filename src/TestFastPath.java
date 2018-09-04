import graphs.*;

public class TestFastPath 
{
    public static void main(String[] args)
    {
        Graph<Place> g= new ListGraph<Place>();
        
        Place platsB= new Place("B");
        Place platsF= new Place("F");
        Place platsA= new Place("A");
        Place platsC= new Place("C");
        Place platsD= new Place("D");
        Place platsG= new Place("G");
        Place platsE= new Place("E");
        
        g.add(platsB);
        g.add(platsF);
        g.add(platsA);
        g.add(platsC);
        g.add(platsD);
        g.add(platsG);
        g.add(platsE);

        g.connect(platsB, platsA, "flyg", 3);
        g.connect(platsB, platsF, "buss", 5);
        g.connect(platsB, platsC, "fötter", 11);
        g.connect(platsA, platsD, "tuben", 12);
        g.connect(platsA, platsC, "buss", 7);
        g.connect(platsC, platsD, "cykel", 12);
        g.connect(platsC, platsG, "moppe", 2);
        g.connect(platsF, platsG, "teleport", 2);
        g.connect(platsG, platsD, "bil", 6);
        g.connect(platsD, platsE, "tanke", 1);
        g.connect(platsG, platsE, "tåg", 15);
        
        System.out.println(g);
        
        g.setConnectionWeight(platsA, platsB, -5); //3
        System.out.println(g);
        
        for(Place p : g.getNodes())
        {
            System.out.print(p+" ");
        }
        
        System.out.println("\n"+g.getEdgeBetween(platsF, platsG));
        
        System.out.println(GraphMethods.pathExist(g, platsB, platsE));
        System.out.println(GraphMethods.getPath(g, platsB, platsE));
    }
}