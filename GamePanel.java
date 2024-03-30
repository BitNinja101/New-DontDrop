import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

/**
   A component that displays all the game entities
*/

public class GamePanel extends JPanel implements Runnable {
    
    Ball ball,ball2;
    Slider slider;
    BrickWall bwall;
    //private Timer timer;
	public ArrayList<Ball> balls;
	public boolean gexit;
    private Graphics g;
    private static int count;
	private int fcount,scount,mcount;
	private Random random;
    
    private SoundManager soundManager;
    private boolean isRunning;
    private boolean isPaused,launch;
    private boolean ballDropped;
    private Thread gameThread;
    private BufferedImage image;
    private Image backgroundImage;
	private Image gameoverImage;
	private Image gamewonImage;
	private Image livesImage;
	private int timer,speed,enter;

	
    
    public GamePanel() {
		random = new Random();
		balls = new ArrayList<>();
		this.speed=60;
        ball = null;
		gexit = false;
        //ball2 = null;
        slider = null;
        bwall = null;
        count = 0;
		this.enter = 0;
        ballDropped = false;
	    isRunning = false;
	    isPaused = false;
		launch = false;
        soundManager = SoundManager.getInstance();
		timer = 4000;
		this.fcount = 0;

	    backgroundImage = ImageManager.loadImage ("nbeach (1).png");
		gameoverImage = ImageManager.loadImage ("Gover1.png");
		gamewonImage = ImageManager.loadImage ("winner.png");
		livesImage = ImageManager.loadImage ("heart_sharp.png");
	    image = new BufferedImage (700, 700, BufferedImage.TYPE_INT_RGB);//Size of actual image
    }
    
    public void createGameEntities() {
       slider = new Slider (this, 350, 650,70,10); 
       bwall = new BrickWall(this);
       ball = new Ball (this, 350, 300,30,slider,bwall,0); 
	   soundManager.playClip("background", true);
    } 

	public int getLives (){
		return ball.getLives ();
	}
    
    public void run () {
	  try {
	       isRunning = true;
	       while (isRunning) {
			    timer--;
		   		if (!isPaused){
					gameUpdate();
            	}
				gameRender();
				Thread.sleep (speed);
				//timer--;	
				if ((timer==0)||(ball.getLives()==0)||(getCount()==32)){
					endGame();
				}
	       }
        }
        catch(InterruptedException e) {}
    } 
 	
   public void gameUpdate() {
	    ball.erase();
		boolean multi=ball.getMulti();
		if(multi){
            for(Ball b1:balls){
				b1.erase();
			}
		}

		multi=ball.getMulti();
		if(multi){
            for(Ball b1:balls){
				b1.move();
			}
		}
		if(!launch){
	       ball.move();
		}
		boolean fast=ball.getFast();
		if(fast){
			speed=30;
			fcount++;
		}
		if(fcount==300){
			speed = 60;
			ball.setFast();
			fcount=0;
		}
        boolean slow=ball.getSlow();
		if(slow){
			speed=120;
			scount++;
		}
		if(scount==100){
		    speed = 60;
			ball.setSlow();
			scount=0;
		}
        multi=ball.getMulti();
		int rIndex = random.nextInt(2);
		if(multi){
			//speed=120;
			if(enter==0){
				//int rIndex = random.nextInt(2);
				if(rIndex==0){
			    balls.add(new Ball (this, 300, 300,30,slider,bwall,1));
			    }
				else{
					balls.add(new Ball (this, 300, 300,30,slider,bwall,1));
					balls.add(new Ball (this, 330, 350,30,slider,bwall,1));
				}
			} 
			//balls.add(new Ball (this, 350, 300,30,slider,bwall)); 
			mcount++;
			enter++;
		}
		if(mcount==200){
			ball.setMulti();
			int val = balls.size();
			for(int i=val-1;i>=0;i-- ){
				balls.remove(i);
			}
			mcount = 0;
			enter  = 0;
		}
   }   

   public void updateSlider (int direction) {
	  if (slider != null && !isPaused) {
	    slider.move(direction);
	  }
   }

    public void updateBall (int direction) {
	  if(ball != null && !isPaused) {
	    ball.slide(direction);
	  }
    }

    public void checkLaunch(){
	  launch = ball.getLaunch();
	  if(launch){
		ball.Launch();
	  }
    }

   public boolean getLaunch(){
	  launch = ball.getLaunch();
	  return launch;
    }
   
   public void gameRender() {

	    // draw the game objects on the image

	    Graphics2D imageContext = (Graphics2D)image.getGraphics();
 
	    imageContext.drawImage(backgroundImage, 0, 0, null);	// draw the background image
	    int lv = getLives();
	    if(lv>=3){
	        imageContext.drawImage(livesImage, 670, 675, null);
		    imageContext.drawImage(livesImage, 645, 675, null);
		    imageContext.drawImage(livesImage, 620, 675, null);
	    }
	    else if(lv==2){
		    imageContext.drawImage(livesImage, 670, 675, null);
		    imageContext.drawImage(livesImage, 645, 675, null);
	    }
	    else if(lv==1){
		    imageContext.drawImage(livesImage, 670, 675, null);
	    }
		
        boolean col = ball.getCol();

	    if (slider != null) {
		    slider.draw(imageContext);
	    }

	    if ((ball != null)&&( col == false)) {
	       ball.draw(imageContext);
        }  
	    else if((ball != null)&&( col == true)){
		   ball.drawCol(imageContext);
	    }	

	    boolean multi=ball.getMulti();
	    if(multi){
          for(Ball b1:balls){
			col = b1.getCol();
			if ((b1 != null)&&( col == false)) {
					b1.draw(imageContext);
			}  
			else if((b1 != null)&&( col == true)){
					b1.drawCol(imageContext);
			}	
			b1.setCol(false);
		  }
	    }

	   if (bwall != null){
		   bwall.draw(imageContext); 
		   //count++;
        } 
	
	   if ((timer==0)||(ball.getLives()==0)||(gexit==true)){
		    imageContext.drawImage(gameoverImage, 200, 180, null);
	    }

	   if (getCount()==32) {
		   imageContext.drawImage(gamewonImage, 140, 160, null);
	    } 
        
	   Graphics2D g2 = (Graphics2D) getGraphics();	// get the graphics context for the panel
       g2.drawImage(image, 0, 0, 700, 700, null);
	   imageContext.dispose();
	   g2.dispose(); 
	   ball.setCol(false);
    }
   
   
    
    public void loadGame() {	// initialise the game thread 
	   if (gameThread == null) {
			//soundManager.setVolume("background", 0.7f);
			//soundManager.playClip ("background", true);
			createGameEntities();
			gameThread = new Thread (this);	
			gameRender();
	   }
    }

   public void dropBall() {	// start the game by dropping the ball 		
		gameThread.start();
    }
   
   public void restartGame() {	// initialise and start a new game thread 
	  isPaused = false;
      gexit=false;
	  timer = 3000;
	  if (gameThread == null || !isRunning) {
		soundManager.stopClip("gover");
		createGameEntities();
		gameThread = new Thread (this);		
		gameRender();	
	  }
   }
   
   public void pauseGame() {	// pause the game (don't update game entities)
	 if (isRunning) {
		if (isPaused){
	 		isPaused = false;
        }
		else{
			isPaused = true;
        }
	 }
    }
	
    public void endGame() {// end the game thread
		isRunning = false;
		soundManager.stopClip ("background");
		if(getCount()==32){
		    soundManager.playClip("win", true);
		}
		else{
			soundManager.playClip("gover", true);
		}
    }

	

   public int getTimer(){
	  return timer;
   }
   
   public int getCount() {
	  return bwall.getCount();
    }

   public int getPoints() {
	  return bwall.getPoints();
    }

  
}
