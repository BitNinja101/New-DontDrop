import java.awt.event.KeyEvent;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Rectangle;
import javax.swing.JPanel;
import java.awt.Image;

public class Slider {
    private int x, y, dx, dy; // Position of the slider
    private int width, height; // Dimensions of the slider
    private int speed; // Speed of the slider
    private boolean leftPressed, rightPressed; // Flags to track key presses
    
    private JPanel panel;
    private Rectangle2D.Double slider;
    private Color backgroundColour;
    private Dimension dimension;
    private Image sliderImage;
    
    
    public Slider(JPanel p,int x, int y, int width, int height) {
        panel = p;
        dimension = panel.getSize();
        
        backgroundColour = panel.getBackground();
        this.x = x;
        this.y = y;
        
        dx = 10;
        dy = 0;
        
        this.width = width;
        this.height = height;
        
        sliderImage = ImageManager.loadImage ("slider.png");
    }
        
    public void draw (Graphics2D g2) {
      g2.drawImage(sliderImage, x, y, width, height, null);
    }
    
    public void erase () {
      Graphics g = panel.getGraphics ();
      Graphics2D g2 = (Graphics2D) g;

      g2.setColor (backgroundColour);
      g2.fill (new Rectangle2D.Double (x, y, width, height));
      g.dispose();
    }
    
   public void move(int direction) {
      if (!panel.isVisible()) {
        return;
      }
    
       dimension = panel.getSize();

        if(direction == 1) { // move left
          x -= dx;
          if (x < 0) { 
            x = 0; 
          }
        } 
        else if (direction == 2) { // move right
          x += dx;
          if(x + width > dimension.width) { 
            x = dimension.width - width; 
          }
        }
   }
   
   public boolean isOnSlider (int x, int y) {
      if (slider == null){
            return false;
      }
      return slider.contains(x, y);
   }
    
   public Rectangle2D.Double getBounds() {
        return new Rectangle2D.Double(x, y, width, 2);
    }
    
   public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
