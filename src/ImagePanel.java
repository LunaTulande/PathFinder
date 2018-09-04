import javax.swing.*;
import java.awt.*;
public class ImagePanel extends JPanel{
    private ImageIcon image;

    public ImagePanel(String fileName){
	image = new ImageIcon(fileName);
	int imageWidth = image.getIconWidth();
	int imageHeight = image.getIconHeight();
	Dimension dimen = new Dimension(imageWidth,imageHeight);
	setPreferredSize(dimen);
	setMaximumSize(dimen);
	setMinimumSize(dimen);
    }

    @Override
    protected void paintComponent(Graphics g){
	super.paintComponent(g);
	g.drawImage(image.getImage(), 0, 0, this);
    }
}//ImagenPanel