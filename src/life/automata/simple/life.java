package life.automata.simple;

import java.awt.Color;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;
import java.awt.Point;

public final class life implements Runnable{

	private static life Life = new life();
	
	private Stack<cell> cells = null;
	private HashMap<Point,cell> hashCell = null;
	
	public int minx = Integer.MAX_VALUE;
	public int miny = Integer.MAX_VALUE;
	public Stack<cell> getCells() {
		return cells;
	}

	public void setCells(Stack<cell> cells) {
		this.cells = cells;
	}

	public int maxx = Integer.MIN_VALUE;
	public int maxy = Integer.MIN_VALUE;
	
	
	
	private life()
	{
		
	}
	
	public void addCell(int x, int y, Color colour)
	{
		cell c = new cell(x,y);
		c.alive=true;
		c.myColour=colour;
		
		if(null == cells)
		{
			cells = new Stack<cell>();
		}
		cells.push(c);
	}
	
	public void addCell(int x, int y)
	{
		cell c = new cell(x,y);
		c.alive=true;
		
		if(null == cells)
		{
			cells = new Stack<cell>();
		}
		cells.push(c);
	}
	
	public void removeAllCells()
	{
		cells = null;
		cells = new Stack<cell>();
	}
	
	public void findBounds()
	{
		minx = Integer.MAX_VALUE;
		miny = Integer.MAX_VALUE;
		maxx = Integer.MIN_VALUE;
		maxy = Integer.MIN_VALUE;
		if(null == cells)
		{
			minx = 0;
			miny = 0;
			maxx = 0;
			maxy = 0;
			return;
		}
		for(int i=0;i < cells.size();i++)
		{
			cell c = cells.get(i);
			if(maxx < c.x)
			{
				maxx = c.x;
			}
			if(maxy < c.y)
			{
				maxy = c.y;
			}
			if(minx > c.x)
			{
				minx = c.x;
			}
			if(miny>c.y)
			{
				miny = c.y;
			}
		}
		//we should check the size of what is here
		if((maxx - minx) < 100)
		{
			//make the x space at least 200 or so
			int diffa = 50 * (int) Math.floor((double)(maxx - minx) / 2.0);
			int diffb = 50 * (int) Math.ceil((double)(maxx - minx) / 2.0);
			maxx += diffa;
			minx -= diffb;
		}
		if((maxy - miny) < 100)
		{
			//make the x space at least 200 or so
			int diffa = 50 * (int) Math.floor((double)(maxy - miny) / 2.0);
			int diffb = 50 * (int) Math.ceil((double)(maxy - miny) / 2.0);
			maxy += diffa;
			miny -= diffb;
		}
	}
	
	
	public synchronized void doATurn () 
	{
		minx = Integer.MAX_VALUE;
		miny = Integer.MAX_VALUE;
		maxx = Integer.MIN_VALUE;
		maxy = Integer.MIN_VALUE;
		
		System.out.println("Number of Cells " + cells.size());
		Set<Point> keys=null;
		
		hashCell = new HashMap<Point,cell>(9);
		
		while(cells.size() > 0)
		{
			cell c = cells.pop();
			if(!c.alive)
			{
				c.dispose();
				continue;
			}
			
			//System.out.println(c.ID);
			
			for(int i = c.x - 1 ; i <= c.x + 1 ; i++)
			{
				for(int j = c.y - 1 ; j <= c.y + 1 ;j++)
				{
					if(i == c.x && j == c.y)
					{
						//We are on the current cell
						cell gotc = hashCell.get(c.ID);
						if(null == gotc)
						{
							/*Save Object*/
							hashCell.put(c.ID, c);
						}
						else
						{
							/*Object Exists throw this copy away*/
							c.dispose();
							//existed must report as alive
							gotc.alive=true;
						}
						continue;
					}
					
					/*throw away cell*/
					//c.dispose();
					
					cell gotc = hashCell.get(new Point(i,j));
					if(null == gotc)
					{
						gotc = new cell(i,j);
						gotc.myColour=c.myColour;
						hashCell.put(gotc.ID, gotc);
					}
					
					cell.addNeighbor(gotc,c.myColour);
				}/*inner j loop*/
			}/*outter i loop*/
		}/*while stack loop*/
		
		//create the new stack
		cells = new Stack<cell>();
		keys = hashCell.keySet();
		for(Point key : keys)
		{
			cell c = hashCell.get(key);
			if(cell.pushToStack(c))
			{
				if(maxx < c.x)
				{
					maxx = c.x;
				}
				if(maxy < c.y)
				{
					maxy = c.y;
				}
				if(minx > c.x)
				{
					minx = c.x;
				}
				if(miny>c.y)
				{
					miny = c.y;
				}
				cells.push(c);
			}
			else
			{
				c.dispose();
			}
		}
		
		//we should check the size of what is here
		if((maxx - minx) < 100)
		{
			//make the x space at least 200 or so
			int diffa = 50 * (int) Math.floor((double)(maxx - minx) / 2.0);
			int diffb = 50 * (int) Math.ceil((double)(maxx - minx) / 2.0);
			maxx += diffa;
			minx -= diffb;
		}
		if((maxy - miny) < 100)
		{
			//make the x space at least 200 or so
			int diffa = 50 * (int) Math.floor((double)(maxy - miny) / 2.0);
			int diffb = 50 * (int) Math.ceil((double)(maxy - miny) / 2.0);
			maxy += diffa;
			miny -= diffb;
		}
		
		hashCell.clear();
		hashCell = null;
		
		/*
		for(Point key : keys)
		{
			Object obj = hashCell.get(key);
		    WeakReference ref = new WeakReference<Object>(obj);
		    obj = null;
		    while(ref.get() != null) {
		    	System.gc();
		     }	
		}
		*/
		     
		   
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int generation = 0;
		
		for(;;)
		{
				
			if(null == cells || cells.size() < 1)
			{
				//the game is over
				break;
			}
			
			System.out.println("============================== " + generation++ + " " + cells.size() + " " + minx + "," + miny + " "+ maxx + "," + maxy);
			
			doATurn();
			/*
			try
			{
				Thread.sleep(1000);
			}
			catch(Exception e)
			{
				
			}
			*/
		}/*outer for loop*/
		
	}

	public static life getLife()
	{
		return Life;
	}
	
	public static void main(String[] args)
	{
		life l = getLife();
		
		
		/* Stable Cross */
		//l.addCell(0,1);
		//l.addCell(0,2);
		//l.addCell(0,3);
		
		/* A Glider */
		//l.addCell(5,5);
		//l.addCell(6,6);
		//l.addCell(6,7);
		//l.addCell(5,7);
		//l.addCell(4,7);
		
		
		for(int i = 0; i < 1000;i++)
		{
			for(int j=0; j < 1000 ; j++)
			{
				if(Math.random() > 0.6)
				{
					l.addCell(i,j);
				}
			}
		}
		
		Thread t = new Thread(l);
		t.start();
	}
}
