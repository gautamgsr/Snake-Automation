import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


/**
 *
 * @author Gautam Rawat
 */
public class snake 
{

	public static void main(String[] args) 
        {
		
		 new GameFrame();
	}
}
class GamePanel extends JPanel implements ActionListener
{

	static final int SCREEN_WIDTH = 800;
	static final int SCREEN_HEIGHT = 800;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
	static final int DELAY = 100;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 2;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
    boolean[][] visited;
	Timer timer;
	Random random;       
	
	GamePanel()
        {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
                visited = new boolean[(SCREEN_HEIGHT/UNIT_SIZE)+1][(SCREEN_WIDTH/UNIT_SIZE)+1];
		startGame();
	}
	public void startGame() 
        {
		newApple();
		running = true;
		timer = new Timer(DELAY,this);  // this method calls itself(this) after particular interval(DELAY)
		timer.start();
	}
        public void moveItself() 
        {
            // function that decides the next direction based upon the co-ordinates of apple
            // it checks all possible direction in which it can go and also check weather there is any body part or not
            // it calculates distance from all the three direction and choose the move which has minimum distance from the apple.
            float left=Float.MAX_VALUE ;
            float right=Float.MAX_VALUE ;
            float up=Float.MAX_VALUE ;
            float down=Float.MAX_VALUE;
            boolean flage = false;
            
            if(direction!='R' && (x[0]-UNIT_SIZE)/UNIT_SIZE>0 && !visited[(x[0]-UNIT_SIZE)/UNIT_SIZE][y[0]/UNIT_SIZE]) // check for left
            {
               left = (float)Math.sqrt(Math.pow((appleX-(x[0]-UNIT_SIZE)),2)+Math.pow((appleY-y[0]),2));    
            }
            if(direction!='L' && (x[0]+UNIT_SIZE)<SCREEN_WIDTH && !visited[(x[0]+UNIT_SIZE)/UNIT_SIZE][y[0]/UNIT_SIZE]) // check for right
            {
               right = (float)Math.sqrt(Math.pow((appleX-(x[0]+UNIT_SIZE)),2)+Math.pow((appleY-y[0]),2));     
                
            }
            if(direction!='D' && (y[0]-UNIT_SIZE)/UNIT_SIZE>0 && !visited[x[0]/UNIT_SIZE][(y[0]-UNIT_SIZE)/UNIT_SIZE]) // check for up
            {
               up = (float)Math.sqrt(Math.pow((appleX-(x[0])),2)+Math.pow((appleY-(y[0]-UNIT_SIZE)),2));     
            }
            if(direction!='U' && y[0]+UNIT_SIZE<SCREEN_HEIGHT && !visited[x[0]/UNIT_SIZE][(y[0]+UNIT_SIZE)/UNIT_SIZE]) // check for down
            {
                down = (float)Math.sqrt(Math.pow((appleX-(x[0])),2)+Math.pow((appleY-(y[0]+UNIT_SIZE)),2));    
            }
            
            if(left<=right && left<=up && left<=down)
            { 
                direction = 'L'; 
            }
            if(right<=left && right<=up && right<=down)
            {
                direction = 'R';
            }
            if(up<=right && up<=left && up<=down)
            {
                direction = 'U';
            }
            if(down<=right && down<=up && down<=left)
            {
                direction = 'D'; 
            }
        }
	public void paintComponent(Graphics g)
        {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g)
        {
		
		if(running)
                {
			
			for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) 
                        {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
			for(int i = 0; i< bodyParts;i++)
                        {
                                if(i==0)
                                {
                                    g.setColor(Color.red);
                                    g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                                }
                                else
                                {
                                    g.setColor(Color.green);
                                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);    
                                }
                                			
			}
			g.setColor(Color.red);
			g.setFont( new Font("Ink Free",Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		}
		else 
                {
			gameOver(g);
		}
		
	}
	public void newApple() 
        {
                // this method helps to get new random cordinates for the apple.
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;  // it returns a random integer within the range
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	public void move()
        {
               // this method shift all the body parts to one step forward
               // x[0]y[0] contains head of the snake
 		for(int i = bodyParts;i>0;i--)
                {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
                
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
                // this methods keep track of snake body positions 
                // so we can check its body part location while judgeing the direction.
                visited[x[bodyParts]/UNIT_SIZE][y[bodyParts]/UNIT_SIZE] = false;
                try
                {
                   visited[x[0]/UNIT_SIZE][y[0]/UNIT_SIZE] = true; // Marks head as true
                }
                catch(Exception e)
                {
                    System.out.println("error in visited matrix");
                }
                
	}
	public void checkApple() 
        {
                //if head reaches the to apple co-ordinates, body size and score increases.
                // also call newApple() method is invoked. 
		if((x[0] == appleX) && (y[0] == appleY)) 
                {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	public void checkCollisions() 
        {
                
		//checks if head collides with body
		for(int i = bodyParts;i>0;i--)
                {
			if((x[0] == x[i])&& (y[0] == y[i])) 
                        {
				running = false;
			}
		}
		//check if head touches left border
		if(x[0] < 0) 
                {
			running = false;
		}
		//check if head touches right border
		if(x[0] > SCREEN_WIDTH) 
                {
			running = false;
		}
		//check if head touches top border
		if(y[0] < 0) 
                {
			running = false;
		}
		//check if head touches bottom border
		if(y[0] > SCREEN_HEIGHT)
                {
			running = false;
		}
		
		if(!running) 
                {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) 
        {
                System.out.println(x[0]+" "+y[0]);
		//Score
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		//Game Over text
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}
	@Override
	public void actionPerformed(ActionEvent e) 
        {
		
		if(running) 
                {
			move();
                        moveItself();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter
        {
		@Override
		public void keyPressed(KeyEvent e) 
                {
			switch(e.getKeyCode()) 
                        {
                            case KeyEvent.VK_LEFT:
                                    if(direction != 'R')
                                    {
                                            direction = 'L';
                                    }
                                    break;
                            case KeyEvent.VK_RIGHT:
                                    if(direction != 'L') 
                                    {
                                            direction = 'R';
                                    }
                                    break;
                            case KeyEvent.VK_UP:
                                    if(direction != 'D')
                                    {
                                            direction = 'U';
                                    }
                                    break;
                            case KeyEvent.VK_DOWN:
                                    if(direction != 'U') 
                                    {
                                            direction = 'D';
                                    }
                                    break;
			}
		}
	}
}
 class GameFrame extends JFrame
{

	GameFrame()
        {
			
		this.add(new GamePanel());
		this.setTitle("Snake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		
	}
}
