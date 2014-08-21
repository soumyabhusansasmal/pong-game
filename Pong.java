
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

// The Pong! Applet class
public class Pong extends Applet implements MouseMotionListener
{
	// Constants
	int width = 600;
	int height = 300;

	// Entities
	Paddle p1;
	Paddle p2;
	Ball ball;

	// Last update time
	long lastUpdate;

	// Initialize the applet
	public void init()
	{
		// Initialzie entities
		p1 = new Paddle();
		p2 = new Paddle();
		ball = new Ball();
		reset();

		// Register event listener
		addMouseMotionListener(this);

		// First update
		lastUpdate = new Date().getTime();

	}

	// Destroy the applet
	public void destroy()
	{
	}

	// Update
	public void update(Graphics g)
	{
		// Find time elapsed since last update
		long currentTime = new Date().getTime();
		double dt = (currentTime-lastUpdate)/1000.0;
		lastUpdate = currentTime;

		// Finding the tentative position of ball
		double x = ball.x + ball.vx*dt;
		double y = ball.y + ball.vy*dt;

		// Collision Detection
		if( y < 0 ) // Top
		{
			ball.x = x;
			ball.vy = -ball.vy;
		}
		else if( y > height - ball.diameter ) // Bottom
		{
			ball.x = x;
			ball.vy = -ball.vy;
		}
		else if( x < p1.width ) // Left
		{
			if( (y + ball.diameter) > p1.y && (y < p1.y + p1.height) ) // Hit
			{
				ball.y = y;
				ball.vx = -ball.vx;
			}
			else	// Miss
			{
				p2.score++;
				reset();
			}
		}
		else if( x > width-p2.width-ball.diameter ) // Right
		{
			if( (y + ball.diameter) > p2.y && (y < p2.y + p2.height) ) // Hit
			{
				ball.y = y;
				ball.vx = -ball.vx;
			}
			else	// Miss
			{
				p1.score++;
				reset();
			}
		}
		else // No collision
		{
			ball.x = x;
			ball.y = y;
		}

		// Artificial Intelligence
		int dy = (int)(dt * 2*ball.velocity/3);
		double b = ball.y+ball.diameter/2;
		double p = p2.y+p2.height/2;
		if( ball.vx < 0 ) // Move towards center
		{
			if( Math.abs(p2.y-height/2) <= dy )
				;
			else if( p2.y < height/2 )
				p2.y += dy;
			else
				p2.y -= dy;
		}
		else if ( ball.vx > 0 ) // Move towards ball
		{
			if( p2.y < b )
				p2.y += dy;
			else
				p2.y -= dy;
		}

		// Double Buffering
		Image buffer = createImage(width,height);
		paint(buffer.getGraphics());
		g.drawImage(buffer,0,0,this);
	}

	// Paint graphics on screen
	public void paint(Graphics g)
	{
		// Draw background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0,0,width-1,height-1);

		// Draw entities
		p1.paint(g);
		p2.paint(g);
		ball.paint(g);

		// Show score
		g.setColor(Color.BLUE);
		g.drawString("Score: " + p1.score + " - " + p2.score, 260,150);

		// Request re-paint for next frame
		repaint();
	}

	// Reset entities
	private void reset()
	{
		// Position
		ball.x = width/2  - ball.diameter/2;
		ball.y = height/2 - ball.diameter/2;
		p1.x = 0;
		p1.y = height/2 - p1.height/2;
		p2.x = width-p1.width;
		p2.y = height/2 - p2.height/2;
		// Velocity
		ball.vx = -(Math.random() * ball.velocity/2 + ball.velocity/2);
		ball.vy = Math.sqrt(ball.velocity*ball.velocity-ball.vx*ball.vx);
	}

	// Handle mouse move
	public void mouseMoved(MouseEvent e)
	{
		p1.y = e.getY() - p1.height/2;
	}

	// Handle mouse drag
	public void mouseDragged(MouseEvent e)
	{
		p1.x = e.getX() - p1.width/2;
	}
}

// The Paddle Class
class Paddle
{
	// Position
	int x,y;
	// Dimensions
	int width = 10;
	int height = 60;
	// Score
	int score=0;

	// Draw it
	public void paint(Graphics g)
	{
		g.setColor(Color.BLUE);
		g.fillRect(x,y,width,height);
	}


}

// The Ball Class
class Ball
{
	// Position
	double x,y;
	// Dimensions
	int diameter = 10;
	// Velocity (px/sec)
	double velocity = 350;
	double vx, vy;

	// Draw it
	public void paint(Graphics g)
	{
		g.setColor(Color.RED);
		g.fillOval((int)x,(int)y,diameter,diameter);
	}
}
