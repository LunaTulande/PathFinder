import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import javax.swing.filechooser.*;
import graphs.*;
import java.util.*;

public class PathFinder extends JFrame{
    private final JButton[] BUTTONS= new JButton[5];
    private final JMenuItem[] MENUITEMS= new JMenuItem[5];
    private Graph<Place> places;
    private JFileChooser fileChooser= new JFileChooser("Maps");
    private ImagePanel image= null;
    private MusLyss ml=new MusLyss();
    private Place p1, p2; 
    
    public PathFinder(){
        super("PathFinder");
        
        JMenuBar menuBar= new JMenuBar();
        JMenu menuArchive= new JMenu("Archive");
        menuBar.add(menuArchive);
        JMenuItem chooseNew= new JMenuItem("New");
        chooseNew.addActionListener(new ChooseNewLyss());
        menuArchive.add(chooseNew);
        JMenuItem chooseExit= new JMenuItem("Exit");
        chooseExit.addActionListener(new ChooseExitLyss());
        menuArchive.add(chooseExit);
        JMenu menuActions= new JMenu("Actions");
        menuBar.add(menuActions);
        this.setJMenuBar(menuBar);
        
        JPanel buttons= new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        add(buttons, BorderLayout.NORTH); 
        
        String[] buttonsAndItemsName= {"Find Path","Show Connection","New Place","New Connection","Change Connection"};
        
        for(int i=0; i<5; i++){
            JMenuItem m= new JMenuItem(buttonsAndItemsName[i]);
            m.setEnabled(false);
            menuActions.add(m);
            MENUITEMS[i]= m;
            
            JButton b= new JButton(buttonsAndItemsName[i]);
            b.setEnabled(false);
            buttons.add(b);
            BUTTONS[i]=b;
        }//for
        MENUITEMS[0].addActionListener(new FindPathLyss());
        BUTTONS[0].addActionListener(new FindPathLyss());
        MENUITEMS[1].addActionListener(new ShowConnectionLyss());
        BUTTONS[1].addActionListener(new ShowConnectionLyss());
        MENUITEMS[2].addActionListener(new NewPlaceLyss());
        BUTTONS[2].addActionListener(new NewPlaceLyss());
        MENUITEMS[3].addActionListener(new NewConnectionLyss());
        BUTTONS[3].addActionListener(new NewConnectionLyss());
        MENUITEMS[4].addActionListener(new ChangeConnectionLyss());
        BUTTONS[4].addActionListener(new ChangeConnectionLyss());   
        
        FileNameExtensionFilter filter= new FileNameExtensionFilter("Images", "png", "jpg", "gif");
        fileChooser.setFileFilter(filter);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(400,300);
        pack();
        setVisible(true);
    }//Constructor
    
    private class ChooseNewLyss implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ave){   
            places= new ListGraph<>();
            p1=null; p2=null;
            enableDisableButtonsAndItems();
            int svar= fileChooser.showOpenDialog(PathFinder.this);
            if(svar != JFileChooser.APPROVE_OPTION)
            {    return; }
            File file= fileChooser.getSelectedFile();
            String filePath= file.getAbsolutePath();
            if(image != null)
            {    remove(image); }
            image= new ImagePanel(filePath);
            image.setLayout(null);
            add(image);
            validate();
            pack();
        }
    }//class chooseNewLyss

    private class ChooseExitLyss implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) 
        { System.exit(0); }
    }//class chooseExitLyss
    
    private class NewPlaceLyss implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ave){
            Cursor kryss = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
	    image.setCursor(kryss);
            image.addMouseListener(ml);
        }
    }//newPlaceLyss
    
    private class MusLyss extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent mev){
            JPanel form= new JPanel();
            form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
            form.add(new JLabel("Place's name:"));
            JTextField nameField= new JTextField(10); 
            form.add(nameField);
            for(;;){
                boolean fieldEmpty=true; 
                while(fieldEmpty){
                    int option= JOptionPane.showConfirmDialog(PathFinder.this, form, "New Place", JOptionPane.OK_CANCEL_OPTION);
                    String name= nameField.getText();
                    if(option == JOptionPane.OK_OPTION){
                        if(!name.equals("")){
                            try{
                                Integer.parseInt(name);
                                JOptionPane.showMessageDialog(PathFinder.this,"Incorrect name!","Wrong", JOptionPane.ERROR_MESSAGE);
                            }catch(NumberFormatException ex){
                                fieldEmpty=false;
                                boolean placeExist=true;
                                for(Place pl : places.getNodes())
                                {
                                    if(pl.getName().equals(name)){
                                        JOptionPane.showMessageDialog(PathFinder.this, "There is already a place with this name!", "Obs", JOptionPane.INFORMATION_MESSAGE);
                                        placeExist=false;
                                        break;
                                    }  
                                }//for
                                if(placeExist){
                                    Place p= new Place(name,mev.getX(), mev.getY());
                                    places.add(p);
                                    p.addMouseListener(new KlickLyss()); 
                                    image.add(p);
                                    image.validate();
                                    image.repaint(); 
                                }//if
                            }//try-catch
                        }//if name isn't null
                        else{
                            JOptionPane.showMessageDialog(PathFinder.this,"Name's field is empty!!","Wrong", JOptionPane.ERROR_MESSAGE);}
                    }//if ok_option
                    else{
                        return;}
                }//while
                break;//for
            }//for
            image.setCursor(Cursor.getDefaultCursor());
            image.removeMouseListener(ml);
        }//mouseClicked
    }//MusLyss
    
    private class KlickLyss extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent mev){
            Place p= (Place)mev.getSource();
            if(p1 != null && p.equals(p1)){
                p.setVald(false);
                p1=p2;
                p2=null; 
            }
            else if(p2 != null && p.equals(p2)){ 
                p.setVald(false);
                p2=null; 
            }       
            else if (p1 == null && p != p2){
                p1 = p;
		p.setVald(true);
	    }
	    else if (p2 == null && p != p1){
                p2 = p;
		p.setVald(true);
	    }
            enableDisableButtonsAndItems();
        }//mouseClicked
    }//KlickLyss

    private void enableDisableButtonsAndItems(){
        if(p1 != null && p2 != null){
            goThroughButtonsAndItems(true); }
        else{
            goThroughButtonsAndItems(false);}
    }
    private void goThroughButtonsAndItems(boolean setEnabled){
        for(int i=0; i<5; i++){
            if(i != 2){
                MENUITEMS[i].setEnabled(setEnabled);
                BUTTONS[i].setEnabled(setEnabled);
                }
                MENUITEMS[2].setEnabled(!setEnabled);//NewPlace
                BUTTONS[2].setEnabled(!setEnabled);
            }//for
    }
    
    private class NewConnectionLyss implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ave){
            JPanel form= new JPanel();
            form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
            form.add(new JLabel("Connection from "+p1+" to "+p2));
            JPanel namePanel= new JPanel();
            namePanel.add(new JLabel("Name: "));
            form.add(namePanel);
            JTextField nameField= new JTextField(10);
            namePanel.add(nameField);
            JPanel timePanel= new JPanel();
            timePanel.add(new JLabel("Time: "));
            form.add(timePanel);
            JTextField timeField= new JTextField(3);
            timePanel.add(timeField);
            timePanel.add(new JLabel("h"));
            
            Edge e= places.getEdgeBetween(p1, p2);
            if (e != null){ 
                JOptionPane.showMessageDialog(PathFinder.this, "There is already a connection between these two places: "+p1+" "+p2+"!", "Obs", JOptionPane.INFORMATION_MESSAGE);}
            else{
                for(;;)
                {
                 boolean fieldEmpty=true; 
                 while(fieldEmpty)
                 {
                   int option= JOptionPane.showConfirmDialog(PathFinder.this, form, "New Connection", JOptionPane.OK_CANCEL_OPTION);
                   if(option == JOptionPane.OK_OPTION){
                    try{
                    String name= nameField.getText();
                    int time= Integer.parseInt(timeField.getText());
                
                    if(!name.equals("") && time>0){
                        fieldEmpty=false;
                        places.connect(p1, p2, name, time); 
                    }else{
                        JOptionPane.showMessageDialog(PathFinder.this,"Name's field is empty or time's not aceptable!","Wrong", JOptionPane.ERROR_MESSAGE); }
                    }catch(NumberFormatException ex){
                        JOptionPane.showMessageDialog(PathFinder.this,"Error input at name's field or time's field","Wrong", JOptionPane.ERROR_MESSAGE); }
                  }//if ok_option
                   else{
                       return;}
                 }//while
                 break;//for
                }//for
            }//else about pathExist
            
        }//actionPerformed
    }//NewConnectionLyss 
    
    private class ChangeConnectionLyss implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ave){
            Edge e= places.getEdgeBetween(p1, p2);   
            if(e!=null){
                JPanel form= new JPanel();
                form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
                form.add(new JLabel("Connection from "+p1.getName()+" to "+p2.getName()));
                JPanel namePanel= new JPanel();
                namePanel.add(new JLabel("Name: "));
                form.add(namePanel);
                JTextField nameField= new JTextField(10);
                nameField.setText(e.getName());
                nameField.setEditable(false);
                namePanel.add(nameField);
                JPanel timePanel= new JPanel();
                timePanel.add(new JLabel("Time: "));
                form.add(timePanel);
                JTextField timeField= new JTextField(3);
                timeField.setText(Integer.toString(e.getWeight()));
                timePanel.add(timeField);
                timePanel.add(new JLabel("h"));
                for(;;){
                    boolean fieldEmpty=true; 
                    while(fieldEmpty){
                        int option= JOptionPane.showConfirmDialog(PathFinder.this, form, "Connection's information", JOptionPane.OK_CANCEL_OPTION);
                        if(option==JOptionPane.OK_OPTION){
                            try{
                                int time= Integer.parseInt(timeField.getText());
                                if(time>0){
                                    places.setConnectionWeight(p1, p2, time); 
                                    fieldEmpty=false; }
                                else{
                                    JOptionPane.showMessageDialog(PathFinder.this,"Time's not aceptable!","Wrong", JOptionPane.ERROR_MESSAGE); }
                            }catch(NumberFormatException ex){
                                JOptionPane.showMessageDialog(PathFinder.this,"Error input at time's field","Wrong", JOptionPane.ERROR_MESSAGE); }
                        }//if ok_option
                        else{
                            return;}
                    }//while
                    break;//for
                }//for
            }//if e isn't null
            else{
                JOptionPane.showMessageDialog(PathFinder.this, "There is no connection between these two places!", "Obs", JOptionPane.INFORMATION_MESSAGE); }
        }//actionPerformed
    }//ChangeConnectionLyss
    
    private class ShowConnectionLyss implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ave){
            Edge e= places.getEdgeBetween(p1, p2);   
            if(e!=null){
                JPanel form= new JPanel();
                form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
                form.add(new JLabel("Connection from "+p1+" to "+p2));
                JPanel namePanel= new JPanel();
                namePanel.add(new JLabel("Name: "));
                form.add(namePanel);
                JTextField nameField= new JTextField(10);
                nameField.setText(e.getName());
                nameField.setEditable(false);
                namePanel.add(nameField);
                JPanel timePanel= new JPanel();
                timePanel.add(new JLabel("Time: "));
                form.add(timePanel);
                JTextField timeField= new JTextField(3);
                timeField.setText(Integer.toString(e.getWeight()));
                timeField.setEditable(false);
                timePanel.add(timeField);
                timePanel.add(new JLabel("h"));
                
                JOptionPane.showMessageDialog(PathFinder.this, form, "Connection's information", JOptionPane.INFORMATION_MESSAGE);
            }//if e isn't null
            else{
                JOptionPane.showMessageDialog(PathFinder.this, "There is no connection between these two places!", "Obs", JOptionPane.INFORMATION_MESSAGE); }
        }//actionPerformed
    }//showConnectionLyss
    
    private class FindPathLyss implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ave){
            try{
            ArrayList<Edge> path= new ArrayList<Edge>(GraphMethods.getPath(places, p1, p2));
            
            JPanel form= new JPanel();
            JTextArea textArea= new JTextArea();
            String str="From "+p1+" to "+p2+":";
            int pathTime= 0;
            for(Edge e : path){
               pathTime+= e.getWeight();
               str+= "\n"+e; 
            }
            str+= "\nTotal time "+pathTime;
            textArea.setText(str);
            form.add(textArea);
            
            JOptionPane.showMessageDialog(PathFinder.this, form, "Fast Path", JOptionPane.INFORMATION_MESSAGE);
            }catch(NullPointerException e){
                JOptionPane.showMessageDialog(PathFinder.this, "There is no way between these two places!", "Obs", JOptionPane.INFORMATION_MESSAGE);
            }//try-catch
        }//actionPerformed
    }//FindPathLyss
    
    public static void main(String[] args)
    { new PathFinder(); }
}//PathFinder