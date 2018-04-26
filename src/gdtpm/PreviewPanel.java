package gdtpm;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * @author Sonic
 */
public class PreviewPanel extends JPanel {
    public PreviewPanel(){
        super();
        super.setDoubleBuffered(true);
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
    }
    
    public void paintImage(BufferedImage img, Graphics g){
        g.drawImage(img, 0, 0, this);
        super.validate();
        super.repaint();
    }
    
}
