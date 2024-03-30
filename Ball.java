import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.Dimension;
import java.util.Random;
import java.util.ArrayList;

public class Ball {
   // instance variables - replace the example below with your own

   //private Image alienImage;
   private int x, y, dx, dy, topY; // Position of the ball
   private int intialDy;
   private int diameter; // Diameter of the ball
   private JPanel panel;
   private Ellipse2D.Double ball;
   private Color backgroundColour;
    
   private int spbX, spbW,tracker,lives;
    
   private Slider slider;
   private ArrayList<Power> powerUps = new ArrayList<>();
   private BrickWall bwall;
   private long lastCollisionTime = 0;
   private static final long COLLISION_COOLDOWN = 500;
   private long bricklastCollisionTime = 0;
   private static final long COLLISION_BRICKCOOLDOWN = 00;

   private boolean col;

   private boolean fast;
   private boolean slow;
   private boolean multi;
   private boolean launch,reset,rcol;

     
   private Dimension dimension;
   private int version;
    
   boolean isRunning;
   Random random;
   Random random1;
    
   private SoundManager soundManager;
   private Image ballImage;
   private Image ballImage2;

   public Ball(JPanel p,int x, int y, int diameter,Slider sal,BrickWall bw,int version) {
      col = false;
      panel = p;
      dimension = panel.getSize();
      backgroundColour = panel.getBackground ();
      this.x = x;
      this.y = y;
      this.slider = sal;
      this.bwall = bw;
      this.version = version;
      //this.powerUps = this.powerUps;
        
      random1 = random = new Random();
        
      spbX = spbW = 0;
      topY = y;
    
      dx = 0;            
      intialDy = dy = 12;          
        
      this.diameter = diameter;
      soundManager = SoundManager.getInstance();
      //tracker = 3000;
      lives = 3;
      ballImage = ImageManager.loadImage ("newbb.png");
      ballImage2 = ImageManager.loadImage ("newbb2.png");

      this.fast = false;
      this.slow = false;
      this.multi = false;
      this.launch = false;
      this.reset = false;
      this.rcol = false;
   }

   public void setLocation() {
      int panelWidth = panel.getWidth();
      //x = random.nextInt (panelWidth - diameter); 
      //y=200;
      x = slider.getX() + 15;
      y = slider.getY() - 40;
      lives--;
      dx = 0;
      dy = 0;
      launch = true;
      reset = true;
   }

   public void Launch(){
      dy = -12;
      launch = false;
   }

   public void slide(int direction) {
      if (!panel.isVisible()) {
         return;
      }
      x = slider.getX() + 15;
   }

   public boolean getLaunch(){
      return launch;
   }
    
   public void draw (Graphics2D g2) {
      g2.drawImage(ballImage, x, y, diameter, diameter, null);
      if(powerUps!=null){
         for(Power powerUp : powerUps){
            if(powerUp!=null){
               powerUp.draw(g2);
            }
         }
      }
   }

   public void drawCol (Graphics2D g2) {
      g2.drawImage(ballImage2, x, y, diameter, diameter, null);
   }

   
   
   public void move() {
      if (!panel.isVisible()){ 
         return;
      }

      if((reset==true)&&(rcol==false)){}
      else{
         x = x + dx;
      }
      y = y + dy;
      
      int panelWidth = panel.getWidth();
      int panelHeight = panel.getHeight();

      // Check for collision with left and right walls
      if (x <= 0 || x >= panelWidth - diameter) {
         dx = -dx;
      }

      // Check for collision with top wall 
      if (y <= 0) {
         dy = -dy;
      }

      // Check for collision with bottom wall 
      if ((y >= panelHeight - diameter)&&(version==0)) {
         soundManager.playClip("lose", false);
         setLocation();
      } 
        
      // Check for collision with slider
      boolean collision = collidesWithBall();
      if (collision && System.currentTimeMillis() - lastCollisionTime >= COLLISION_COOLDOWN) {
         soundManager.playClip("hit", false);
         ballPhysics();
         lastCollisionTime = System.currentTimeMillis();
         col = true;
         rcol = true;
         reset = false;
      } 
        
      // Check for collision with brick
      boolean collision1 = collidesWithBrick();
      if (collision1 && System.currentTimeMillis() - bricklastCollisionTime >= COLLISION_BRICKCOOLDOWN) {
         soundManager.playClip("hit", false);
         brickPhysics();
         bricklastCollisionTime = System.currentTimeMillis();
         col = true;
         rcol = true;
         reset = false;
      } 

      if(powerUps!=null){
         for(Power powerUp : powerUps){
            if(powerUp!=null){
               powerUp.move();//Power if mover
               //Slider power up collision
               boolean collision2 = collidesWithPower(powerUp);
               if(collision2) {
                  soundManager.playClip("appear", false);
                  powerUp.setY();
                  int t = powerUp.getType();
                  if (t==0){
                     fast=true;
                  }
                  else if (t==1){
                     slow=true;
                  }
                  else if (t==2){
                     if(lives<3){
                        lives++;
                     }
                  }
                  else if (t==3){
                     multi=true;
                  }
               }                         
            }
         }
      }

   } 
     
   public Rectangle2D.Double getBounds() {
      return new Rectangle2D.Double(x, y, diameter, diameter);
   }
    
   public void erase () {
      Graphics g = panel.getGraphics ();
      Graphics2D g2 = (Graphics2D) g;
      g2.setColor (backgroundColour);
      g2.fill (new Ellipse2D.Double (x, y, diameter, diameter));

      g.dispose();
   }
    
   public boolean collidesWithBall() {
      Rectangle2D.Double ballRect = getBounds();
      Rectangle2D.Double sliRect = slider.getBounds();
      return sliRect.intersects(ballRect); 
   }

   public boolean collidesWithPower(Power powerUp) { //Edit this 
      Rectangle2D.Double powerRect = powerUp.getBounds();
      Rectangle2D.Double sliRect = slider.getBounds();
      return sliRect.intersects(powerRect); 
   }
   
   public boolean collidesWithBrick() {
      for (Brick brick : bwall.bricks) {
         if(brick.getVisible()==true){
            Rectangle2D.Double ballRect = getBounds();
            Rectangle2D.Double brickRect = brick.getBounds();
            if(brickRect.intersects(ballRect) == true){
               brick.setVisible(false);
               int val = brick.getType();
               brick.erase(panel);
               bwall.setPoints();
               spbX = brick.getX();
               int spbY = brick.getY();
               spbW = brick.getWidth();
               if((val==0)||(val==2)){
                  int typ = random.nextInt(4);
                  powerUps.add(new Power(spbX,spbY,slider,typ));//creation of power-UP
               }
               return true;
            } 
         }   
      }
      return false;
   }
   
   public void ballPhysics() {
      int sliderCenterX = slider.getWidth()/ 2;
      sliderCenterX = sliderCenterX + slider.getX();
      int ballCenterX = diameter / 2;
      ballCenterX= ballCenterX + x;
       
      dy = intialDy;
       
      //double angle = Math.toRadians(60); // Adjust the angle 
      double angle = Math.toRadians(60); // fix brick collision
      int newDx = (int) (dy * Math.sin(angle)); // Calculate new horizontal velocity
      int newDy = (int) (dy * Math.cos(angle)); // Calculate new vertical velocity

      dx = newDx;
      dy = -newDy; 

      // prevents multiple collisions
      if (ballCenterX < sliderCenterX) {
         dx = Math.abs(dx);   
      } 
      else if (ballCenterX > sliderCenterX){
         dx = -Math.abs(dx);
      }
      else{
         dx=0;
      }
   } 
   
   public void brickPhysics() {
      int brickCenterX =  spbW/ 2;
      brickCenterX = brickCenterX + spbX;
      int ballCenterX = diameter / 2;
      ballCenterX= ballCenterX + x;
       
      dy = intialDy;
      dy = dy * -1;
       
      //double angle = Math.toRadians(60); // Adjust the angle 
      double angle = Math.toRadians(60); // Adjust the angle 
      int newDx = (int) (dy * Math.sin(angle)); // Calculate new horizontal velocity
      int newDy = (int) (dy * Math.cos(angle)); // Calculate new vertical velocity

       
      dx = newDx;
      dy = -newDy; 

      if (ballCenterX < brickCenterX) {
         dx = Math.abs(dx);   
      } 
      else if (ballCenterX > brickCenterX){
         dx = -Math.abs(dx);
      }
      else{
         dx=0;
      }
   } 

   public boolean getCol(){
     return col;
   }

   public void setCol(boolean value){
     col = value;
   }

   public void setDy(int val){
      dy = val;
    }

   public int getLives (){
     return lives;
   }

   public boolean getFast(){
      return fast;
   }

   public void setFast(){
      fast = false;
   }

   public boolean getSlow(){
      return slow;
   }

   public void setSlow(){
      slow = false;
   }

   public boolean getMulti(){
      return multi;
   }

   public void setMulti(){
      multi = false;
   }
   
}
