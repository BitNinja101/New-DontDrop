import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import java.awt.Image;


public class Brick {
    private int x, y, type,point; 
    private int width, height; 
    private boolean isVisible; 
    Random random;
    private Image brickImage;
    private Image specialbrickImage;
    private Image hardImage;
    
    private Rectangle2D.Double brick;

    public Brick(int x, int y,int type) {
        this.type=type;
        this.point = 0;
        this.x = x;
        this.y = y;
        this.width = 60;
        this.height = 20;
        isVisible = true;
        random = new Random();
        brickImage = ImageManager.loadImage ("brick.png");
        hardImage = ImageManager.loadImage ("hard.png");
        specialbrickImage = ImageManager.loadImage ("special.png");
    }

    public void draw (Graphics2D g2) {
        if(type==1){
            g2.drawImage(hardImage, x, y, width, height, null);
        }
        else if((type==0)||(type==2)){
            g2.drawImage(specialbrickImage, x, y, width, height, null);
        }
        else{
            g2.drawImage(brickImage, x, y, width, height, null);
        }
    }
    
    public Rectangle2D.Double getBounds() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    public boolean getVisible() {
        return isVisible;
    }
    
    public void erase (JPanel panel) {
      Graphics g = panel.getGraphics ();
      Graphics2D g2 = (Graphics2D) g;

      g2.setColor(Color.GRAY);
      g2.fill (new Rectangle2D.Double (x, y, width, height));

      g.dispose();
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getType() {
        return type;
    }

    public int getPoint() {
        if(type==1){
            point = 10;
        }
        else if((type==0)||(type==2)){
            point = 5;
        }
        else{
            point = 1;
        }
        return point;
    }
}
