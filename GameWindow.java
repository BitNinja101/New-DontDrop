import javax.swing.*;			// need this for GUI objects
import java.awt.*;			// need this for Layout Managers
import java.awt.event.*;		// need this to respond to GUI events

public class GameWindow extends JFrame implements ActionListener,  KeyListener, MouseListener {
  // declare instance variables for user interface objects

  // declare labels 

  private JLabel statusBarL;
  private JLabel keyL;
  private JLabel mouseL;

  // declare text fields

  private JTextField statusBarTF;
  private JTextField keyTF;
  private JTextField mouseTF;

  // declare buttons

  private JButton startB;
  private JButton pauseB;
  private JButton endB;
  private JButton dropB;
  private JButton restartB;
  private JButton exitB;

  private Container c;

  private JPanel mainPanel;
  private GamePanel gamePanel;
    
  private int keyText= 0;
  private int lives;
    
  @SuppressWarnings({"unchecked"})    
  public GameWindow() {
        
    setTitle ("Don't Drop The Ball V2!");
    setSize (800, 850);

    // create user interface objects

    // create labels
    statusBarL = new JLabel ("Application Status: ");
    keyL = new JLabel("Points Obtained / Lives: ");
    mouseL = new JLabel("Time Left: ");
    
    // create text fields and set their colour, etc.

    statusBarTF = new JTextField (25);
    keyTF = new JTextField (25);
    mouseTF = new JTextField (25);

    statusBarTF.setEditable(false);
    keyTF.setEditable(false);
    mouseTF.setEditable(false);

    statusBarTF.setBackground(Color.CYAN);
    keyTF.setBackground(Color.YELLOW);
    mouseTF.setBackground(Color.GREEN);
        
    // create buttons

    startB = new JButton ("Load Game");
    pauseB = new JButton ("Pause Game");
    endB = new JButton ("End Game");
    restartB = new JButton ("Start New Game");
    dropB = new JButton ("Drop Ball");
    exitB = new JButton ("Exit");

    // add listener to each button (same as the current object)

    startB.addActionListener(this);
    pauseB.addActionListener(this);
	  endB.addActionListener(this);
	  restartB.addActionListener(this);
    dropB.addActionListener(this);
    exitB.addActionListener(this);
        
    // create mainPanel

    mainPanel = new JPanel();
    FlowLayout flowLayout = new FlowLayout();
    mainPanel.setLayout(flowLayout);

    GridLayout gridLayout;

    // create the gamePanel for game entities

    gamePanel = new GamePanel();
    gamePanel.setPreferredSize(new Dimension(700, 700)); // change size of game panel
    gamePanel.setBackground(Color.GRAY);
    //gamePanel.createGameEntities();
    
    // create infoPanel

    JPanel infoPanel = new JPanel();
    gridLayout = new GridLayout(3, 2);
    infoPanel.setLayout(gridLayout);
    infoPanel.setBackground(Color.ORANGE);
        
    // add user interface objects to infoPanel
    
    infoPanel.add (statusBarL);
    infoPanel.add (statusBarTF);

    infoPanel.add (keyL);
    infoPanel.add (keyTF);        

    infoPanel.add (mouseL);
    infoPanel.add (mouseTF);
        
    // create buttonPanel

    JPanel buttonPanel = new JPanel();
    gridLayout = new GridLayout(2, 3);
    buttonPanel.setLayout(gridLayout);

    buttonPanel.add (startB);
    buttonPanel.add (dropB);
    buttonPanel.add (pauseB);
	  buttonPanel.add (endB);
	  buttonPanel.add (restartB);
    buttonPanel.add (exitB);

    // add sub-panels with GUI objects to mainPanel and set its colour

    mainPanel.add(infoPanel);
    mainPanel.add(gamePanel);
    mainPanel.add(buttonPanel);
    mainPanel.setBackground(Color.GRAY);

    // set up mainPanel to respond to keyboard and mouse

    gamePanel.addMouseListener(this);
    mainPanel.addKeyListener(this);
        
    // add mainPanel to window surface

    c = getContentPane();
    c.add(mainPanel);

    // set properties of window

    setResizable(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    setVisible(true);
    dropB.setVisible(false);

    // set status bar message

    statusBarTF.setText("Application Started");
  } 

  // implement single method in ActionListener interface
  public void actionPerformed(ActionEvent e) {

	  String command = e.getActionCommand();
		
		//statusBarTF.setText(command + " button clicked.");

		if (command.equals(startB.getText())) {
      statusBarTF.setText("Game Loaded");
			gamePanel.loadGame();
      lives = gamePanel.getLives();
      mouseTF.setText("" + gamePanel.getTimer());
      keyTF.setText(keyText + " POINTS / " + Lives());
      startB.setVisible(false);
      dropB.setVisible(true);
		}

    if (command.equals(dropB.getText())) {
      statusBarTF.setText("Game Running");
			gamePanel.dropBall();
      dropB.setVisible(false);
		} 

	  if(command.equals(pauseB.getText())) {
			gamePanel.pauseGame();
      lives = gamePanel.getLives();
      mouseTF.setText("" + gamePanel.getTimer());
      keyTF.setText(keyText + " POINTS / " + Lives());
		  
		}

		if(command.equals(endB.getText())) {
      mouseTF.setText("GIVING UP ALREADY!?");
      keyTF.setText(keyText + " POINTS / GAME OVER" );
      statusBarTF.setText("Game Stopped");
      gamePanel.gexit = true;
			gamePanel.gameRender();
      gamePanel.endGame();
      
		}

		if(command.equals(restartB.getText())){
			gamePanel.restartGame();
      dropB.setVisible(true);
    }

		if (command.equals(exitB.getText())){
			System.exit(0);
    }
		mainPanel.requestFocus();
  }
    
  // implement methods in KeyListener interface

  public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
    lives = gamePanel.getLives();
    mouseTF.setText(""+ gamePanel.getTimer());
    keyTF.setText(keyText + " POINTS / "+ Lives());
    keyText = gamePanel.getPoints();
    int bricks = gamePanel.getCount();
		  
    if(bricks==32){
      mouseTF.setText(":} WINNER WINNER CHICKEN DINNER :)");
      keyTF.setText(keyText + " POINTS!!! GAME OVER");
      statusBarTF.setText("Game Stopped.");
        
    }
    else if(lives<=0){
      mouseTF.setText(":{ NO MORE LIVES YOU LOSE :(");
      keyTF.setText(keyText + " POINTS GAME OVER" );
      statusBarTF.setText("Game Stopped");   
    }
    else if(gamePanel.getTimer()<=0){
      mouseTF.setText(":{ TIME UP YOU LOSE :(");
      keyTF.setText(keyText + " POINTS GAME OVER" );
      statusBarTF.setText("Game Stopped");
    }

		if(keyCode == KeyEvent.VK_LEFT) {
		  gamePanel.updateSlider (1);
		}

    if (keyCode == KeyEvent.VK_RIGHT) {
      gamePanel.updateSlider (2);
		}

    if (keyCode == KeyEvent.VK_SPACE) {
      gamePanel.checkLaunch();
		}

    boolean launch = gamePanel.getLaunch();
    if((keyCode == KeyEvent.VK_LEFT)&&(launch)) {
		  gamePanel.updateBall (1);
		}

    if((keyCode == KeyEvent.VK_RIGHT)&&(launch)) {
      gamePanel.updateBall (2);
		}
  }
  
  public void mouseClicked(MouseEvent e) {}

  public void keyReleased(KeyEvent e) {}

  public void keyTyped(KeyEvent e) {}
    
  public void mouseEntered(MouseEvent e) {}

  public void mouseExited(MouseEvent e) {}

  public void mousePressed(MouseEvent e) {}

  public void mouseReleased(MouseEvent e) {}

  public String Lives(){
    String s="";
    if(lives>=3){
      s = "X X X LIVES";
    }
    if(lives==2){
      s = "X X LIVES";
    }
    else if(lives==1){
      s = "X LIVES";
    }
    return s;
  } 
}
