import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import java.awt.Image;

public class Power {
    private Image fastImage;
    private Image slowImage;
    private Image multiImage;
    private Image heartImage;
    private int x,y,type;
    private int width,height;
    private SoundManager soundManager;

    public Power(int x, int y,Slider sal,int type){
        soundManager = SoundManager.getInstance();
        fastImage = ImageManager.loadImage ("fast.png");
        slowImage = ImageManager.loadImage ("slow.png");
        multiImage = ImageManager.loadImage ("multi.png");
        heartImage = ImageManager.loadImage ("heart_sharp.png");
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = 50;
        this.height = 50;
    }

    public void draw (Graphics2D g2) {
        if(type==0){
            g2.drawImage(fastImage, x, y, width,height, null);
        }
        else if(type==1){
            g2.drawImage(slowImage, x, y, width, height, null);
        }
        else if(type==2){
            g2.drawImage(heartImage, x, y, 30, 30, null);
        }
        else{
            g2.drawImage(multiImage, x, y, width, height, null);
        }
    }

    public void move() {
        y = y + 10;
    } 

    public Rectangle2D.Double getBounds() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    public void setY() {
        y = y + 1000;
    } 

    public int getType() {
        return type;
    } 
    
}
