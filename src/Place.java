import java.awt.*;
import javax.swing.*;

public class Place extends JComponent{
    private String name;
    private boolean select= false;
    
    public Place(String name)//para el TestFastPath
    {
        this.name= name;
    }
    
    public Place(String name, int x, int y){
        this.name= name;
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBounds(x, y, 80, 30); }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawString(name,0,getHeight()-5);
        if(!select){
            g.setColor(Color.BLUE);
            g.fillOval(0,0,15,15);
        }else{
            g.setColor(Color.RED);
            g.fillOval(0,0,15,15);
        }
    }//paintComponent
    
    public void setVald(boolean b){
	select = b;
	repaint();
    }

    public boolean isVald(){ return select; }
    
    public String getName() { return name; }
    
    @Override
    public int hashCode() { return name.hashCode(); }
    
    @Override
    public boolean equals(Object other){
        if(other instanceof Place){    
            Place p= (Place)other;
            return this.name.equals(p.name);
        }else{
            return false; }
    }//equals
    
    @Override
    public boolean contains(int x, int y){
        return x > 0 && x < 15 && y > 0 && y < 15; }
    
    @Override
    public String toString() { return name; }
}//Place