import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class BrickWall {
    public ArrayList<Brick> bricks;
    private JPanel panel;
    private Dimension dimension;
    private int numBricks,points;
    Random random;

    public BrickWall(JPanel p) {
        panel = p;
        bricks = new ArrayList<>();
        random = new Random();
        
        // Add bricks to the wall (Brick Layout)
        for (int i = 0; i < 4; i++) {
            //int rIndex = random.nextInt(4);
            for (int j = 0; j < 8; j++) {
                int rIndex = random.nextInt(7);
                bricks.add(new Brick(90 * j , 40 * i,rIndex ));
                //90 j 25 i
            }
        }
        numBricks = 0;
        //numBricks = 31;
        points = 0;
        
    }

    public void draw(Graphics2D g2) {
        numBricks=0;
        for (Brick brick : bricks) {
           if(brick.getVisible()){  
               brick.draw(g2);  
           }
           else{
             numBricks++;
           }
        }
    }
  
    public int getCount(){
       return numBricks;
    }
    
    public ArrayList<Brick> getBricks(){
       return bricks;
    }
    
    public int getPoints(){
       return points;
    }
    
    public void setPoints(){
        points = 0;
        for (Brick brick : bricks) {
            if(brick.getVisible()== false){  
                points = points + brick.getPoint();  
            }
         }
    }
}


