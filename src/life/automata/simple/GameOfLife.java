package life.automata.simple;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Stack;

public final class GameOfLife extends Applet implements Runnable, MouseMotionListener, WindowListener,MouseListener,KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	life Life = null;
	
	boolean suspend = false;
	boolean editMode = false;
	boolean editZoomMode = false;
	
	public Image doubleBuffer = null;
	private Image oldDoubleBuffer = null;
	private Font myFont = new Font("TimesRoman", Font.BOLD, 18);
	
	public void init()
	{
		Life = life.getLife();
		this.setBackground(Color.black);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		this.requestFocus();
	}
	
	public void RandomGame()
	{
		
		/*
		for(int i = 0 ; i < 30 ; i++)
		{
			Life.addCell(i,0);
		}
		*/
		
		int col = 0;
		Color[] arr={Color.red,Color.green,Color.blue /*,Color.cyan,Color.orange,Color.PINK,Color.yellow,Color.white */};
		//Life.removeAllCells();
		/*
		for(int i=0;i<3;i++)
		{
			col = (col + 1) % arr.length;
			int y=i *100;
			
			Life.addCell(3,0 + y,arr[col]);
			Life.addCell(4,1 + y,arr[col]);
			Life.addCell(0,2 + y,arr[col]);
			Life.addCell(4,2 + y,arr[col]);
			Life.addCell(1,3 + y,arr[col]);
			Life.addCell(2,3 + y,arr[col]);
			Life.addCell(3,3 + y,arr[col]);
			Life.addCell(4,3 + y,arr[col]);
			
			Life.addCell(0,7 + y,arr[col]);
			Life.addCell(1,8 + y,arr[col]);
			Life.addCell(2,8 + y,arr[col]);
			Life.addCell(2,9 + y,arr[col]);
			Life.addCell(2,10 + y,arr[col]);
			Life.addCell(1,11 + y,arr[col]);
			
			Life.addCell(3,14 + y,arr[col]);
			Life.addCell(4,15 + y,arr[col]);
			Life.addCell(0,16 + y,arr[col]);
			Life.addCell(4,16 + y,arr[col]);
			Life.addCell(1,17 + y,arr[col]);
			Life.addCell(2,17 + y,arr[col]);
			Life.addCell(3,17 + y,arr[col]);
			Life.addCell(4,17 + y,arr[col]);
		}
		*/
		
		int sep = 40;
		for(int x = 0 ; x < 7 ; x++)
			for(int y = 0; y < 7 ; y++)
			{
				col = (col+1) % arr.length;
				for(int i = 0; i < 30;i++)
					for(int j=0; j < 30 ; j++)
					{
						if(Math.random() > 0.5)
						{
							//System.out.println("Add a Cell " + x*sep  + i + " " + y*sep + j);
							Life.addCell(x*sep + i,y*sep + j,arr[col]);
						}
					}
			}
		
		
		/*
		 012345678901
		0############
		1# # # # #  #
		2#         ##
		3##         #
		4#         ##
		5##         #
		6#         ##
		7##         #
		8#         ##
		9##         #
		0#  # # # # #
		1############
		*/
		/*
		Life.addCell(2, 1);
		Life.addCell(4, 1);
		Life.addCell(6, 1);
		Life.addCell(8, 1);
		
		Life.addCell(1, 3);
		Life.addCell(1, 5);
		Life.addCell(1, 7);
		Life.addCell(1, 9);
		
		Life.addCell(3, 10);
		Life.addCell(5, 10);
		Life.addCell(7, 10);
		Life.addCell(9, 10);
		
		Life.addCell(10, 2);
		Life.addCell(10, 4);
		Life.addCell(10, 6);
		Life.addCell(10, 8);
		
		for(int i = 1 ; i < 11; i++)
		{
			Life.addCell(i,0);
			Life.addCell(0,i);
			Life.addCell(i,11);
			Life.addCell(11,i);
		
		}
		Life.addCell(0, 0);
		Life.addCell(11,11);
		Life.addCell(11, 0);
		Life.addCell(0, 11);
		*/
		Life.findBounds();
	}
	
	public void update(Graphics g)
	{
		paint(g);
	}
	
	public void paint(Graphics g)
	{		
		//g.clearRect(0, 0, this.getWidth(), this.getHeight());
		if(null != oldDoubleBuffer)
		{
			g.drawImage(oldDoubleBuffer,0,0,null);
		}
		
		if(commandMode)
		{
			
			g.clearRect(0, 0, this.getWidth() , 70);
		
			g.setColor(Color.CYAN);
		
			g.setFont(myFont);
			
			g.drawString("Enter Command",20,20);
			if(!editMode)
			{
				g.drawString("R - Reset Game, B - Birth Toggle, S - Survive Toggle, E - Edit Mode", 20, 35);
			}
			else
			{
				g.setColor(Color.YELLOW);
				g.drawString("R - Reset Game, B - Birth Toggle, S - Survive Toggle, E - Exit Edit Mode", 20, 35);
			}
			
			if(toggleBirth)
			{
				g.setColor(Color.orange);
				String birth = "Birth : ";
				for(int i = 0 ; i < cell.NumBirth.length ; i++)
				{
					if(cell.NumBirth[i])
					{
						birth = birth + " " + i;
					}
					else
					{
						birth = birth + "  ";
					}
				}
				g.drawString(birth, 170, 20);
			}
			
			else if(toggleSurvive)
			{
				g.setColor(Color.magenta);
				String birth = "Survive : ";
				for(int i = 0 ; i < cell.NumSurvive.length ; i++)
				{
					if(cell.NumSurvive[i])
					{
						birth = birth + " " + i;
					}
					else
					{
						birth = birth + "  ";
					}
				}
				g.drawString(birth, 170, 20);
			}
		}
		
		if(dragging)
		{
			g.setColor(Color.white);
			
			g.drawLine(x1, y1, x1, y2);
			g.drawLine(x1, y1, x2, y1);
			g.drawLine(x2, y1, x2, y2);
			g.drawLine(x1, y2, x2, y2);
		}
		
	}
	
	synchronized private void toggleCell(int x,int y)
	{
		Stack<cell> cells = Life.getCells();
		Stack<cell> newCell = new Stack<cell>();
		boolean found = false;
		
		for(int i=0;i < cells.size();i++)
		{
			cell c = cells.get(i);
			
			if(c.x == x && c.y == y)
			{
				found = true;
				continue;
			}
			
			newCell.push(c);
		}
		if(!found)
		{
			cell c = new cell(x,y);
			c.alive=true;
			c.myColour=Color.WHITE;
			
			newCell.push(c);
		}
		Life.setCells(newCell);
	}
	
	public void draw(Graphics p_g)
	{
		int scaleX = 1;
		int scaleY = 1;
		
		Graphics g = null;
		
		//there should be bounds
		int width =  this.getWidth();
		int height = this.getHeight();
		
		if(width < 1 || height < 1)
		{
			return;
		}
		
		/*TODO - look into why the if was commented out*/
		//if(null == doubleBuffer)
		//{
			doubleBuffer=createImage(width,height);
			g = doubleBuffer.getGraphics();
		//}
		
		if(dragging || commandMode)
		{
			//update blinking rectangle
			repaint(0);
			pause(100);
		}
			
		g.clearRect(0, 0, width, height);
		
		int unitsX = Life.maxx - Life.minx + 1;
		int unitsY = Life.maxy - Life.miny + 1;
		
		if(inView)
		{
			unitsX = bx2 - bx1 + 1;
			unitsY = by2 - by1 + 1;
			if(!editMode)
			{
				System.out.println(bx1 +" "+ by1 +" "+ bx2 + " " + by2);
			}
		}
		
		if(unitsX < 1 || unitsY < 1)
		{
			return;
		}
		
		if(unitsX < width)
		{
			scaleX = (int)Math.round(width/unitsX);	
		}
		
		if(unitsY < height)
		{
			scaleY = (int)Math.round(height/unitsY);
		} 
		
		Stack<cell> cells = Life.getCells();
		
		for(int i=0;i < cells.size();i++)
		{
			cell c = cells.get(i);
		
			int x = c.x - Life.minx;
			int y = c.y - Life.miny;
		
			if(inView)
			{
				x = c.x - bx1;
				y = c.y - by1;
			}
			
			double ddx = (double)x/(double)unitsX * (double)width;
			double ddy = (double)y/(double)unitsY * (double)height;
			
			int xx = (int)Math.floor(ddx);
			int yy = (int)Math.floor(ddy);
			
			int xx1 = (int)Math.ceil(ddx) - xx + scaleX;
			int yy1 = (int)Math.ceil(ddy) - yy + scaleY;
			
			/*
			 * This may be usable in the future
			if(!c.alive)
			{
				continue;
			}
			*/
			
			/*
			if(inView)
			{
				if(xx < bx1 || xx > bx2 || yy < by1 || yy > by2)
				{
					continue;
				}
			}
			*/
			g.setColor(c.myColour);
			g.fillRect(xx, yy, xx1, yy1);
		}
		
		/*
		if(dragging)
		{
			g.setColor(Color.white);
			
			g.drawLine(x1, y1, x1, y2);
			g.drawLine(x1, y1, x2, y1);
			g.drawLine(x2, y1, x2, y2);
			g.drawLine(x1, y2, x2, y2);
		}
		*/
		p_g.drawImage(doubleBuffer,0,0,null);
		oldDoubleBuffer = doubleBuffer;
		doubleBuffer = null;
		if(dragging || commandMode)
		{
			//update blinking rectangle
			repaint(0);
			pause(100);
		}
	}
	
	public void start()
	{
		RandomGame();
		Life.findBounds();
		Thread t = new Thread(this);
		t.start();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
	
		if (dragging)
		{
			x2 = e.getX();
			y2 = e.getY();
		}
		else if(editMode)
		{
			clickThisCell(e.getX(),e.getY(),false);
		}
		repaint(0);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	void pause(int num)
	{
		try
		{
			Thread.sleep(num);
		}
		catch(Exception e)
		{
			
		}
	}
	
	@Override
	public void run() {
		System.out.println("Run the Main Loop");
		// TODO Auto-generated method stub
		boolean wasEditMode = false;
		Graphics g = null;
		
		while(null == g)
		{
			pause(100);
			g = this.getGraphics();
		}
		
		draw(g);
		pause(1000);
		
		int turns = 0;
		while(true)
		{
			spinlock = false;
			if(suspend)
			{
				//This is a safe place to resize the world
				repaint(0);
				pause(100);
				continue;
			}
			
			wasEditMode = true;
			if(!editMode)
			{
				Life.doATurn();
				/*gc();*/
				wasEditMode = false;
			}
			//Some Els
			
			if(gameReset)
			{
				gameReset = false;
				Life.removeAllCells();
				RandomGame();
				turns = 0;
			}
			
			g = this.getGraphics();
			if(null != g)
			{
				draw(g);
			}
			
			if(!wasEditMode)
			{
				System.out.println("Turn Number " + ++turns);
			}
			else
			{
				pause(100);
			}
			/*
			if(Life.getCells().size() < 1000)
			{
				pause(500);
			}
			*/
		}
	}
	
	/*
	 public static void gc() {
	     Object obj = new Object();
	     WeakReference ref = new WeakReference<Object>(obj);
	     obj = null;
	     while(ref.get() != null) {
	       System.gc();
	     }
	   }
	*/
	
	 public static void main(String args[])
	    {
		GameOfLife t = new GameOfLife();

		String D=new String("Life");
		
		System.out.println(D);
		
		Frame f = new Frame(D);
		Dimension dim = new Dimension((640+10),(480+30));
		f.setSize(dim);
		
		f.add("Center",t);
		f.setVisible(true);
		
		f.addWindowListener(t);
			
		t.init();
	       
		t.start();
	    }

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		clickThisCell(e.getX(),e.getY(),true);
	}

	int editX = 0;
	int editY = 0;
	
	private void clickThisCell(int px,int py,boolean clearit)
	{
		//there should be bounds
		
		if(!editMode)
			return;
		
		int width =  this.getWidth();
		int height = this.getHeight();
		
		if(width < 1 || height < 1)
		{
			return;
		}
		
		int unitsX = Life.maxx - Life.minx + 1;
		int unitsY = Life.maxy - Life.miny + 1;
			
		if(inView)
		{
			unitsX = bx2 - bx1 + 1;
			unitsY = by2 - by1 + 1;
			if(!editMode)
			{
				System.out.println(bx1 +" "+ by1 +" "+ bx2 + " " + by2);
			}
		}
		
		int xUnit = unitsX * px/width;
		int yUnit = unitsY * py/height;
		
		int cellX = xUnit + Life.minx;
		int cellY = yUnit + Life.miny;
		
		if(inView)
		{
			cellX = xUnit + bx1;
			cellY = yUnit + by1;
		}
		
		if(!clearit)
		{
			if(editX == cellX && editY == cellY)
				return;
		}
		
		editX = cellX;
		editY = cellY;
		
		System.out.print("Edit Unit (" + xUnit + "," + yUnit + ") of (" + unitsX + " x " + unitsY +"). ");
		System.out.println("Toggle Cell (" + cellX +"," + cellY +")");
		toggleCell(cellX,cellY);
		/*
		Graphics g = this.getGraphics();
		if(null != g)
		{
			draw(g);
		}*/		
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	int x1 = 0;
	int x2 = 0;
	int y1 = 0;
	int y2 = 0;
	boolean inView = false;
	
	int bx1 = 0;
	int bx2 = 0;
	int by1 = 0;
	int by2 = 0;
	
	boolean dragging = false;
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(!editMode || editZoomMode)
		{
			x1 = e.getX();
			y1 = e.getY();
			x2 = x1;
			y2 = y1;
			System.out.println("PRESS!!!!!");
			dragging = true;
		}
	}

	boolean spinlock = false;
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if(!editMode || editZoomMode)
		{
			dragging = false;
			// TODO Auto-generated method stub
			x2 = e.getX();
			y2 = e.getY();
			
			int button=e.getButton();
			if(button != MouseEvent.BUTTON1)
			{
				inView = false;
				return;
			}
			
			if (x1 > x2)
			{
				int tmp = x1;
				x1 = x2;
				x2 = tmp;
			}
			if(y1 > y2)
			{
				int tmp = y1;
				y1 = y2;
				y2 = tmp;
			}
			int width = this.getWidth();
			int height = this.getHeight();
			
			if(width < 1 || height < 1)
			{
				return;
			}
			
			suspend=true;
			spinlock = true;
			while (spinlock)
			{
				pause(100);
			}
			
			Life.findBounds();
			
			int unitsX = Life.maxx - Life.minx + 1;
			int unitsY = Life.maxy - Life.miny + 1;
			
			if(inView)
			{
				unitsX = bx2 - bx1 + 1;
				unitsY = by2 - by1 + 1;
			}
			
			
			if(unitsX < 1 || unitsY < 1)
			{
				return;
			}
		
			
			double xr1 = unitsX * ((double)x1/(double)width) + Life.minx;
			double xr2 = unitsX * ((double)x2/(double)width) + Life.minx;
			double yr1 = unitsY * ((double)y1/(double)height) + Life.miny;
			double yr2 = unitsY * ((double)y2/(double)height) + Life.miny;
			
			if(inView)
			{
				xr1 = unitsX * ((double)x1/(double)width) + bx1;
				xr2 = unitsX * ((double)x2/(double)width) + bx1;
				yr1 = unitsY * ((double)y1/(double)height) + by1;
				yr2 = unitsY * ((double)y2/(double)height) + by1;
			}
			
			bx1 = (int) Math.round(xr1);
			bx2 = (int) Math.round(xr2);
			by1 = (int) Math.round(yr1);
			by2 = (int) Math.round(yr2);
			
			inView = true;
			System.out.println("DID IT");
	
			suspend=false;
		}
	}

	boolean gameReset = false;
	
	boolean toggleBirth = false;
	boolean toggleSurvive = false;
	
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		e.consume();
		
		if(key == KeyEvent.VK_SPACE && !commandMode)
		{

			commandMode = true;
			repaint(0);
			return;
		}
		
		if(commandMode)
		{
			if(commandMode && key == KeyEvent.VK_SPACE)
			{
				commandMode = false;
				toggleBirth = false;
				toggleSurvive = false;
				repaint(0);
				return;
			}
			
			if(key == KeyEvent.VK_B)
			{
				toggleBirth = true;
				toggleSurvive = false;
				return;
			}
			
			if(key == KeyEvent.VK_E)
			{
				editMode = !editMode;
				
				if(editMode)
				{
					System.out.println("Push The Cells to a Hash");
				}
				else
				{
					System.out.println("Pop The Cells Out Of Hash");
				}
				
				if(!editMode)
				{
					editZoomMode = false;
				}
			}
			
			if(key >= KeyEvent.VK_0 && key <= KeyEvent.VK_9)
			{
				int num = key - KeyEvent.VK_0;
				try
				{
					if(toggleBirth)
					{
						cell.NumBirth[num] = !cell.NumBirth[num];
					}
					else if(toggleSurvive)
					{
						cell.NumSurvive[num] = !cell.NumSurvive[num];
					}
				}
				catch (Exception exp)
				{
					System.out.println("Toggle Birth/Survive Exception : " + exp.getMessage());
					return;
				}
				repaint(0);
				return;
			}
			
			if(key == KeyEvent.VK_S)
			{
				toggleBirth = false;
				toggleSurvive = true;
				return;
			}
			
			if(key == KeyEvent.VK_R && !editMode)
			{
				commandMode = false;
				gameReset = true;
				inView = false;
				repaint(0);
				return;
			}
			
			toggleBirth = false;
			toggleSurvive = false;
			
		}
		
		if(editMode)
		{
			if(key == KeyEvent.VK_Z)
			{
				editZoomMode = true;
				return;
			}
		}
				
		return;	
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
		int key = e.getKeyCode();
		
		e.consume();
		
		if(editMode)
		{
			if(key == KeyEvent.VK_Z)
			{
				editZoomMode = false;
			}
		}
		
	}

	boolean commandMode = false;
	
	@Override
	public void keyTyped(KeyEvent e) {
	
	}
	
}
